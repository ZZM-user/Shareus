package top.shareus.bot.event;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import org.springframework.stereotype.Component;
import top.shareus.bot.annotation.Group;
import top.shareus.common.core.eumn.GroupEnum;

/**
 * 成员进群请求
 *
 * @author 17602
 */
@Slf4j
@Component
public class MemberJoinRequest extends SimpleListenerHost {

    @EventHandler
    @Group(groupList = {GroupEnum.RES_GROUP, GroupEnum.TEST_GROUP})
    private void onMemberJoinRequest(MemberJoinRequestEvent event) {
        log.debug(event.getMessage());
    }
}
