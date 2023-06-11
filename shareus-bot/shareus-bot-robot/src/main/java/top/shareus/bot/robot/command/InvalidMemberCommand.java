//package top.shareus.bot.robot.command;
//
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import lombok.extern.slf4j.Slf4j;
//import net.mamoe.mirai.Bot;
//import net.mamoe.mirai.console.command.CommandContext;
//import net.mamoe.mirai.console.command.CommandSender;
//import net.mamoe.mirai.console.command.SimpleCommand;
//import net.mamoe.mirai.message.data.MessageChain;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import top.shareus.bot.common.pojo.vo.NormalMemberVO;
//import top.shareus.bot.robot.config.BotManager;
//import top.shareus.bot.robot.config.GroupsConfig;
//import top.shareus.bot.robot.service.GroupService;
//import top.shareus.bot.robot.util.ExcelUtils;
//import top.shareus.bot.robot.util.GroupUploadFileUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * 筛选无效成员
// *
// * @author 17602
// * @date 2022/8/27 19:55
// */
//@Slf4j
//@Component
//public class InvalidMemberCommand extends SimpleCommand {
//	public static final InvalidMemberCommand INSTANCE = new InvalidMemberCommand();
//
//	@Autowired
//	private GroupsConfig groupsConfig;
//	@Autowired
//	private GroupService groupService;
//
//
//	public void onCommand(@NotNull CommandContext context, @NotNull MessageChain args) {
//		Bot bot = BotManager.getBot();
//		CommandSender sender = context.getSender();
//
//		// 率先读取出所有群成员信息
//		Map<String, List<NormalMemberVO>> allGroupMembers = groupService.getAllGroupMembers(bot);
//		List<NormalMemberVO> adminMemberList = allGroupMembers.get("admin");
//		List<NormalMemberVO> resMemberList = allGroupMembers.get("res");
//		List<NormalMemberVO> chatMemberList = allGroupMembers.get("chat");
//
//		log.debug(adminMemberList.size() + "\t" + resMemberList.size() + "\t" + chatMemberList.size());
//		Boolean hasGroups = groupService.invalidGroup(adminMemberList, resMemberList, chatMemberList);
//		try {
//			if (hasGroups) {
//				List<NormalMemberVO> invalidMember = getInvalidMember(adminMemberList, resMemberList, chatMemberList);
//				String filePath = ExcelUtils.exportMemberDataExcel(invalidMember, "资源群失效人员名单");
//				GroupUploadFileUtils.uploadFile(bot.getGroup(groupsConfig.getAdmin().get(0)), filePath);
//			}
//		} catch (Exception e) {
//			log.error("发现异常：", e);
//			sender.sendMessage("操作失败，请联系管理员！");
//		}
//
//	}
//
//	/**
//	 * 获取失效的群成员
//	 *
//	 * @param adminMemberList
//	 * @param resMemberList
//	 * @param chatMemberList
//	 *
//	 * @return
//	 */
//	private List<NormalMemberVO> getInvalidMember(List<NormalMemberVO> adminMemberList, List<NormalMemberVO> resMemberList, List<NormalMemberVO> chatMemberList) {
//		List<NormalMemberVO> invalidMember = new ArrayList<>();
//		for (NormalMemberVO member : resMemberList) {
//			long id = member.getId();
//			String nameCard = member.getNameCard();
//			NormalMemberVO adminMember = adminMemberList.stream().filter(m -> m.getId().equals(id)).findAny().orElse(null);
//			NormalMemberVO chatMember = chatMemberList.stream().filter(m -> m.getId().equals(id)).findAny().orElse(null);
//
//			if (ObjectUtil.isNotNull(adminMember)) {
//				continue;
//			}
//
//			if (! StrUtil.containsAny(nameCard, "①", "②")) {
//				member.setRemark2("备注不规范");
//				invalidMember.add(member);
//				continue;
//			}
//
//			if (ObjectUtil.isNull(chatMember)) {
//				member.setRemark2("不在任一聊天群内");
//				invalidMember.add(member);
//			}
//		}
//		return invalidMember;
//	}
//}
