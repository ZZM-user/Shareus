package top.shareus.bot.robot.config;

import lombok.Getter;
import net.mamoe.mirai.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 机器人配置
 *
 * @author zhaojl
 * @date 2023/01/25
 */
@Getter
@Configuration
public class BotConfig {
	
	@Value("${bot.account}")
	private Long account;
	
	@Value("${bot.password}")
	private String password;
	
	@Autowired
	private RegisterEvents registerEvents;
	
	@Bean
	public Bot getBot() {
		Bot bot = BotManager.createBot(this);
		// 注册事件
		registerEvents.registerGroupMessageEvent();
		registerEvents.registerMemberJoinEvent();
		registerEvents.registerMemberJoinRequestEvent();
		registerEvents.registerFriendMessageEvent();
		registerEvents.registerGroupRecall();
		registerEvents.registerBotJoinGroupEvent();
		
		return bot;
	}
}
