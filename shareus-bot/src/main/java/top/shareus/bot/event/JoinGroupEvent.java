package top.shareus.bot.event;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotJoinGroupEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.config.BotManager;
import top.shareus.bot.config.GroupsConfig;

/**
 * 被邀请进群
 *
 * @author zhaojl
 * @date 2023/03/03
 */
@Slf4j
@Component
public class JoinGroupEvent extends SimpleListenerHost {
	@Autowired
	private GroupsConfig groupsConfig;
	
	@EventHandler
	public void onJoinGroupEvent(BotJoinGroupEvent event) {
		Group group = event.getGroup();
		Bot bot = BotManager.getBot();
		MessageChainBuilder messages = new MessageChainBuilder()
				.append(new At(2657272578L))
				.append("我被邀请进群了-")
				.append(group.getName())
				.append(" - ")
				.append(String.valueOf(group.getId()));
		bot.getGroup(groupsConfig.getTest().get(0)).sendMessage(messages.build());
	}
}
