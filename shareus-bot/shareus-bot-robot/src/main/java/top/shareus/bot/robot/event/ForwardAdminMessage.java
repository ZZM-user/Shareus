package top.shareus.bot.robot.event;

import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.util.MessageChainUtils;

/**
 * 转发管理群组信息
 *
 * @author 17602
 * @date 2022/8/28 16:27
 */
@Slf4j
@Component
public class ForwardAdminMessage extends SimpleListenerHost {
	@Autowired
	private GroupsConfig groupsConfig;
	
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.ADMIN})
	public void onResGroupMessageEvent(GroupMessageEvent event) {
		
		Bot bot = event.getBot();
		// 获取测试组
		Group group = bot.getGroup(groupsConfig.getTest().get(0));
		
		// 构建消息链
		MessageChainBuilder builder = new MessageChainBuilder();
		builder.add(event.getSenderName() + "：");
		
		MessageChain messages = event.getMessage();
		MessageChainUtils.extract(messages, builder);
		// 发送消息
		group.sendMessage(builder.build());
		
	}
	
	@Override
	public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
		log.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
	}
}
