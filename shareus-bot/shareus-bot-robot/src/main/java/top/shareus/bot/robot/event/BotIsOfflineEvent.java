package top.shareus.bot.robot.event;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.BotOfflineEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.shareus.bot.robot.service.impl.ExmailServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * 机器人离线事件
 *
 * @author 17602
 * @date 2023/09/16
 */
@Slf4j
@RefreshScope
@Component
public class BotIsOfflineEvent {
	
	/**
	 * 邮件接收送方邮箱
	 */
	@Value("${spring.mail.mailRecipient}")
	private String mailReceiver;
	
	@Resource
	private ExmailServiceImpl emailService;
	
	@EventHandler
	public void onBotIsOfflineEvent(BotOfflineEvent event) {
		Bot bot = event.getBot();
		log.error("机器人: {} 即将离线，开始发布通知……", bot.getNick());
		
		try {
			List<String> mailReceiverList = List.of(mailReceiver.split(","));
			mailReceiverList.parallelStream()
					.forEach(
							receiver -> emailService.sendSimpleMail(receiver, "机器人离线通知", "机器人: " + bot.getNick() + " 即将离线……\n" + event)
					        );
		} catch (Exception e) {
			log.warn("尝试发出 机器人离线邮件通知时发生错误……", e);
		}
	}
}
