package top.shareus.bot.robot.event;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.robot.annotation.GroupAuth;

/**
 * 有成员离开事件
 *
 * @author 17602
 * @date 2023/06/11
 */
@Slf4j
@Component
public class HasMemberLeaveEvent extends SimpleListenerHost {
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.ADMIN, GroupEnum.CHAT})
	public void onHasMemberLeaveEvent(MemberLeaveEvent event) {
	
	}
}
