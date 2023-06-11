package top.shareus.bot.robot.job;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.pojo.vo.NormalMemberVO;
import top.shareus.bot.robot.config.BotManager;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.service.GroupService;
import top.shareus.bot.robot.util.ExcelUtils;
import top.shareus.bot.robot.util.GroupUploadFileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 清除无效列表
 *
 * @author 17602
 * @date 2023/06/11
 */
@Slf4j
@Component
public class ClearInvalidListEvent {
	@Autowired
	private GroupsConfig groupsConfig;
	@Autowired
	private GroupService groupService;
	
	@Scheduled(cron = "0 0 12 10 * ?")
	public void noticeClear() {
		Bot bot = BotManager.getBot();
		bot.getGroup(groupsConfig.getAdmin().get(0)).sendMessage("晚上九点将会发送本期资源群失效人员名单，为保证名单质量，请于九点前清理聊天群人员。");
		
		log.info("清人提示已发出……");
	}
	
	@Scheduled(cron = "0 0 21 10 * ?")
	public void execute() {
		log.info("开始整理本期资源失效人员名单……");
		Bot bot = BotManager.getBot();
		// 率先读取出所有群成员信息
		Map<String, List<NormalMemberVO>> allGroupMembers = groupService.getAllGroupMembers(bot);
		List<NormalMemberVO> adminMemberList = allGroupMembers.get("admin");
		List<NormalMemberVO> resMemberList = allGroupMembers.get("res");
		List<NormalMemberVO> chatMemberList = allGroupMembers.get("chat");
		
		log.debug(adminMemberList.size() + "\t" + resMemberList.size() + "\t" + chatMemberList.size());
		Boolean hasGroups = groupService.invalidGroup(adminMemberList, resMemberList, chatMemberList);
		try {
			if (hasGroups) {
				List<NormalMemberVO> invalidMember = getInvalidMember(adminMemberList, resMemberList, chatMemberList);
				String filePath = ExcelUtils.exportMemberDataExcel(invalidMember, "资源群失效人员名单");
				GroupUploadFileUtils.uploadFile(bot.getGroup(groupsConfig.getAdmin().get(0)), filePath);
			}
		} catch (Exception e) {
			log.error("发现异常：", e);
		}
		log.info("本期资源失效人员名单已发出");
	}
	
	/**
	 * 获取失效的群成员
	 *
	 * @param adminMemberList
	 * @param resMemberList
	 * @param chatMemberList
	 *
	 * @return
	 */
	private List<NormalMemberVO> getInvalidMember(List<NormalMemberVO> adminMemberList, List<NormalMemberVO> resMemberList, List<NormalMemberVO> chatMemberList) {
		List<NormalMemberVO> invalidMember = new ArrayList<>();
		for (NormalMemberVO member : resMemberList) {
			long id = member.getId();
			String nameCard = member.getNameCard();
			NormalMemberVO adminMember = adminMemberList.stream().filter(m -> m.getId().equals(id)).findAny().orElse(null);
			NormalMemberVO chatMember = chatMemberList.stream().filter(m -> m.getId().equals(id)).findAny().orElse(null);
			
			if (ObjectUtil.isNotNull(adminMember)) {
				continue;
			}
			
			if (! StrUtil.containsAny(nameCard, "①", "②")) {
				member.setRemark2("备注不规范");
				invalidMember.add(member);
				continue;
			}
			
			if (ObjectUtil.isNull(chatMember)) {
				member.setRemark2("不在任一聊天群内");
				invalidMember.add(member);
			}
		}
		return invalidMember;
	}
}
