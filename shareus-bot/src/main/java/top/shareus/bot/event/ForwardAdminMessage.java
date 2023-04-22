package top.shareus.bot.event;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.FlashImage;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import top.shareus.bot.util.GroupUtils;
import top.shareus.bot.util.MessageChainUtils;
import top.shareus.common.core.constant.GroupsConstant;

/**
 * 转发管理群组信息
 *
 * @author 17602
 * @date 2022/8/28 16:27
 */
@Slf4j
@Component
public class ForwardAdminMessage extends SimpleListenerHost {
	@EventHandler
	public void onResGroupMessageEvent(GroupMessageEvent event) {
		long id = event.getGroup().getId();
		
		if (GroupUtils.isAdmin(id)) {
			Bot bot = event.getBot();
			// 获取测试组
			Group group = bot.getGroup(GroupsConstant.TEST_GROUPS.get(0));
			
			// 构建消息链
			MessageChainBuilder builder = new MessageChainBuilder();
			builder.add(event.getSenderName() + "：");
			
			MessageChain messages = event.getMessage();
			MessageChainUtils.extract(messages, builder);
			// 发送消息
			group.sendMessage(builder.build());
		} else {
			// 监听 【所有群组】 闪照
			MessageChain message = event.getMessage();
			// 获取闪照
			FlashImage flashImage = MessageChainUtils.fetchFlashImage(message);
			
			if (ObjectUtil.isNotNull(flashImage)) {
				// 获取测试组
				Bot bot = event.getBot();
				Group group = bot.getGroup(GroupsConstant.TEST_GROUPS.get(0));
				
				MessageChainBuilder builder = new MessageChainBuilder();
				builder.add("【截取的闪照】");
				// 发送者
				builder.add(event.getSenderName());
				// 所在群聊
				builder.add(event.getGroup().getName());
				// 发送时间
				builder.add(DateTime.of(event.getTime()).toDateStr());
				// 闪照
				builder.add(flashImage.getImage());
				
				group.sendMessage(builder.build());
			}
		}
	}
	
	@Override
	public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
		log.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
	}
}
