package top.shareus.bot.robot.aspect;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.common.core.exception.mirai.bot.BotException;

import java.util.HashMap;
import java.util.List;

/**
 * 群组 增强
 *
 * @author zhaojl
 * @date 2023/02/25
 */
@Aspect
@Slf4j
@Component
public class GroupAuthAspect {
	
	@Autowired
	private GroupsConfig groupsConfig;
	
	@Around(value = "@annotation(groupAuth)", argNames = "joinPoint,groupAuth")
	public Object around(ProceedingJoinPoint joinPoint, GroupAuth groupAuth) throws Throwable {
		for (Object arg : joinPoint.getArgs()) {
			if (arg instanceof MessageEvent event) {
				long id = event.getSubject().getId();
				if (hasIt(groupAuth.allowGroupList(), id)) {
					// 恢复程序执行
					return joinPoint.proceed();
				}
			}
		}
		throw new BotException("非管辖范围");
	}
	
	private boolean hasIt(GroupEnum[] groupEnum, Long id) {
		HashMap<GroupEnum, List<Long>> byGroup = groupsConfig.getByGroup();
		for (GroupEnum type : groupEnum) {
			boolean can = byGroup.get(type).stream().anyMatch(id::equals);
			if (can) {
				return true;
			}
		}
		return false;
	}
}
