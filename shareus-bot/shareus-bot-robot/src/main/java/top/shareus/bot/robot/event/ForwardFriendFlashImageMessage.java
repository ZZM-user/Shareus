package top.shareus.bot.robot.event;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.FlashImage;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.util.MessageChainUtils;

/**
 * 转发好友的闪照信息
 *
 * @author 17602
 * @date 2022/11/8:14:26
 */
@Slf4j
@Component
public class ForwardFriendFlashImageMessage extends SimpleListenerHost {
	@Autowired
	private GroupsConfig groupsConfig;
	
	@EventHandler
	public void onFriendFlashImageMessageEvent(FriendMessageEvent event) {
		// 监听 【所有】 闪照
		MessageChain message = event.getMessage();
		// 获取闪照
		FlashImage flashImage = MessageChainUtils.fetchFlashImage(message);
		
		if (ObjectUtil.isNotNull(flashImage)) {
			// 获取测试组
			Bot bot = event.getBot();
			Group group = bot.getGroup(groupsConfig.getTest().get(0));
			
			MessageChainBuilder builder = new MessageChainBuilder();
			builder.add("【截取的闪照】");
			// 发送者
			builder.add(event.getSenderName());
			// 发送时间
			builder.add(DateTime.of(event.getTime()).toDateStr());
			// 闪照
			builder.add(flashImage.getImage());
			
			group.sendMessage(builder.build());
		}
	}
	
	@Override
	public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
		log.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
	}
}
