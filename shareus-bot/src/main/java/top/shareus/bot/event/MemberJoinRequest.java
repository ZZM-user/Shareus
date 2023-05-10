package top.shareus.bot.event;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.annotation.GroupAuth;
import top.shareus.bot.config.GroupsConfig;
import top.shareus.bot.mapper.mapper.BlackListMapper;
import top.shareus.common.core.eumn.GroupEnum;
import top.shareus.common.core.exception.mirai.bot.BotException;
import top.shareus.domain.BlackList;

/**
 * 成员进群请求
 *
 * @author 17602
 */
@Slf4j
@Component
public class MemberJoinRequest extends SimpleListenerHost {
	
	@Autowired
	private BlackListMapper blacklistMapper;
	@Autowired
	private GroupsConfig groupsConfig;
	
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.RES, GroupEnum.CHAT, GroupEnum.TEST})
	public void onMemberJoinRequest(MemberJoinRequestEvent event) {
		log.debug(event.getMessage());
		
		blackListUserFilter(event);
		
	}
	
	/**
	 * 黑名单用户过滤
	 *
	 * @param event 事件
	 */
	private void blackListUserFilter(MemberJoinRequestEvent event) {
		BlackList blacklist = blacklistMapper.selectOne(new LambdaQueryWrapper<BlackList>()
																.eq(BlackList::getQqId, event.getFromId())
																.last(" limit 1"));
		if (ObjUtil.isNotNull(blacklist)) {
			event.reject(true, "机器人认定黑名单用户，如有疑问请联系管理员！");
			net.mamoe.mirai.contact.Group group = event.getBot().getGroup(groupsConfig.getAdmin().get(0));
			String message = " 黑名单用户被拒绝：" + event.getFromId() + " " + event.getFromNick() + "\n来自：" + event.getFromNick();
			group.sendMessage(message);
			log.debug(message);
			throw new BotException(message);
		}
	}
}
