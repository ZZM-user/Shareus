package top.shareus.bot.robot.event;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.data.GroupHonorType;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberHonorChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.common.redis.service.RedisService;

/**
 * 龙王-群荣誉变更事件
 *
 * @author 17602
 * @date 2023-09-14 03:30
 */
@Slf4j
@Component
public class TalkativeHonorChangeEvent extends SimpleListenerHost {
	
	@Autowired
	private RedisService redisService;
	
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.CHAT})
	public void onTalkativeHonorChangeEvent(MemberHonorChangeEvent event) {
		// 获取荣誉类型
		int honorType = event.getHonorType();
		
		log.info("群荣誉变更事件-{}:{} -> {}", event.getHonorType(), event.getMember().getNick(), event.getGroup().getName());
		
		if (honorType == GroupHonorType.TALKATIVE_ID) {
			log.info("群荣誉变更事件-龙王:{} -> {}", event.getMember().getNick(), event.getGroup().getName());
		}
	}
}
