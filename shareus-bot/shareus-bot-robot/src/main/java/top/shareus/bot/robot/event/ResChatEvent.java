package top.shareus.bot.robot.event;

import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.constant.BanResWordConstant;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.bot.robot.config.GroupsConfig;

/**
 * 监听聊天事件 撤销违禁消息
 *
 * @author 17602
 * @date 2022/8/28 9:55
 */
@Slf4j
@Component
public class ResChatEvent extends SimpleListenerHost {
	
	@Autowired
	private GroupsConfig groupsConfig;
	
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.RES})
	public void onAdminGroupMessageEvent(GroupMessageEvent event) {
		long id = event.getGroup().getId();
		
		if (BanResWordConstant.hasBanWord(event.getMessage().contentToString())) {
			// 禁它言
			event.getSender().mute(BanResWordConstant.MUTE_SECONDS);
			// 撤它消息
			MessageSource.recall(event.getMessage());
			String message = "尝试撤回消息 " + event.getSender().getNick() + "：" + event.getMessage().contentToString();
			log.info(message);
			// 通知群
			event.getBot().getGroup(groupsConfig.getAdmin().get(0)).sendMessage(message);
		}
	}
	
	@Override
	public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
		log.error("{}\n{}\n{}", context, exception.getMessage(), exception.getCause().getMessage());
	}
}
