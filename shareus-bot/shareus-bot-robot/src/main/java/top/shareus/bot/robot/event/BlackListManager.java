package top.shareus.bot.robot.event;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.contact.PermissionDeniedException;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.domain.BlackList;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.common.pojo.dto.BlackListSaveDTO;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.bot.robot.config.BotManager;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.service.BlackListService;
import top.shareus.bot.robot.util.MessageChainUtils;

/**
 * 黑名单管理
 *
 * @author 17602
 * @date 2023/06/11
 */
@Slf4j
@Component
public class BlackListManager extends SimpleListenerHost {
	public static final String ORDER = "/拉黑";
	@Autowired
	private BlackListService blackListService;
	@Autowired
	private GroupsConfig groupsConfig;
	
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.ADMIN, GroupEnum.TEST})
	public void onBlackListManager(GroupMessageEvent event) {
		boolean contains = event.getMessage().contentToString().contains(ORDER);
		if (! contains) {
			return;
		}
		
		add(event);
		
	}
	
	private BlackList add(GroupMessageEvent event) {
		BlackListSaveDTO dto = new BlackListSaveDTO();
		MessageChain message = event.getMessage();
		
		Member sender = event.getSender();
		dto.setCreateById(String.valueOf(sender.getId()));
		dto.setCreateBy(sender.getNick() + " " + sender.getNameCard());
		
		PlainText plainText = MessageChainUtils.fetchPlainText(message);
		if (ObjectUtil.isNull(plainText)) {
			log.info("拉黑：提取文本失败");
			return null;
		}
		String content = plainText.contentToString().replace(ORDER, "");
		String targetQQ = ReUtil.get("([0-9]{5,})", content, 1);
		if (StrUtil.isBlank(targetQQ)) {
			log.info("拉黑：提取QQ失败");
			return null;
		}
		
		NormalMember normalMember = event.getGroup().get(Long.parseLong(targetQQ));
		if (ObjectUtil.isNotNull(normalMember)) {
			dto.setNickName(normalMember.getNick() + " " + normalMember.getNameCard());
		}
		dto.setQqId(targetQQ);
		dto.setRemark(content.replace(targetQQ, ""));
		
		BlackList blackList = blackListService.saveBlack(dto);
		
		// 踢人 通知
		Bot bot = BotManager.getBot();
		try {
			groupsConfig.getAll().forEach(id -> {
				Group group = bot.getGroupOrFail(id);
				NormalMember member = group.getOrFail(Long.parseLong(targetQQ));
				member.kick("已经拉黑", true);
				log.info("拉黑踢：{}->{}", id, targetQQ);
			});
		} catch (PermissionDeniedException permissionDeniedException) {
			log.info("没权限踢：{}", permissionDeniedException.getMessage());
		} finally {
			groupsConfig.getAdmin().forEach(id -> {
				Group group = bot.getGroupOrFail(id);
				group.sendMessage("已拉黑，记得踢：" + targetQQ);
			});
		}
		
		return blackList;
	}
}
