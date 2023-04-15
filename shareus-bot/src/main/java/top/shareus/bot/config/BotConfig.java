package top.shareus.bot.config;

import lombok.Data;
import net.mamoe.mirai.Bot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 机器人配置
 *
 * @author zhaojl
 * @date 2023/01/25
 */
@Data
@Configuration
public class BotConfig {

    @Value("${bot.account}")
    private Long account;

    @Value("${bot.password}")
    private String password;

    //    @Bean
    public Bot getBot() {
        Bot bot = BotManager.createBot(this);
        // 注册事件
        RegisterEvents.registerGroupMessageEvent();
        RegisterEvents.registerMemberJoinEvent();
        RegisterEvents.registerFriendMessageEvent();
        RegisterEvents.registerGroupRecall();
        RegisterEvents.registerJoinGroupEvent();
        return bot;
    }
}
