package top.shareus.bot.event;

import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import top.shareus.bot.annotation.GroupAuth;
import top.shareus.common.core.constant.BanResWordConstant;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.common.core.eumn.GroupEnum;

/**
 * 监听聊天事件 撤销违禁消息
 *
 * @author 17602
 * @date 2022/8/28 9:55
 */
@Slf4j
@Component
public class ResChatEvent extends SimpleListenerHost {
	
	@EventHandler
	@GroupAuth(groupList = {GroupEnum.RES_GROUP})
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
			event.getBot().getGroup(GroupsConstant.ADMIN_GROUPS.get(0)).sendMessage(message);
		}
	}
	
	@Override
	public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
		log.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
	}
}
