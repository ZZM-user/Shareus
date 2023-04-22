package top.shareus.bot.aspect;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import top.shareus.bot.annotation.GroupAuth;
import top.shareus.common.core.eumn.GroupEnum;
import top.shareus.common.core.exception.mirai.bot.BotException;

/**
 * 群组 增强
 *
 * @author zhaojl
 * @date 2023/02/25
 */
@Aspect
@Slf4j
@Component
public class GroupAspect {
	
	@Around(value = "@annotation(groupAuth)", argNames = "joinPoint,groupAuth")
	public Object around(ProceedingJoinPoint joinPoint, GroupAuth groupAuth) throws Throwable {
		for (Object arg : joinPoint.getArgs()) {
			if (arg instanceof MessageEvent event) {
				long id = event.getSubject().getId();
				if (groupAuth.groupId() == id || hasIt(groupAuth.groupList(), id)) {
					// 恢复程序执行
					return joinPoint.proceed();
				}
			}
		}
		throw new BotException("非管辖范围");
	}
	
	private static boolean hasIt(GroupEnum[] groupEnum, Long id) {
		for (GroupEnum yeoman : groupEnum) {
			boolean can = yeoman.getGroupList().stream().anyMatch(id::equals);
			if (can) {
				return true;
			}
		}
		return false;
	}
}
