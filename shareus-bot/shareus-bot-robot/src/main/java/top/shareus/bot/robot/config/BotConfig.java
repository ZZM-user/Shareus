package top.shareus.bot.robot.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.mock.MockBotFactory;
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
@Slf4j
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
		if (System.getProperty("os.name").contains("Windows")) {
			MockBotFactory.initialize();
			log.warn("Windows 系统不支持机器人登录，请使用 Linux 系统。已终止此次操作！");
			return MockBotFactory.Companion.newBot(1L, ":");
		}
		
		Bot bot = BotManager.createBot(this);
		// 注册事件
		registerEvents.registerGroupMessageEvent();
		registerEvents.registerMemberJoinEvent();
		registerEvents.registerMemberJoinRequestEvent();
		registerEvents.registerFriendMessageEvent();
		registerEvents.registerGroupRecall();
		registerEvents.registerBotJoinGroupEvent();
		registerEvents.registerMemberHonorChangeEvent();
		registerEvents.registerBotOfflineEvent();
		
		return bot;
	}
}
