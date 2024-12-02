package top.shareus.bot.common.aspect;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import top.shareus.bot.common.annotation.Cache;
import top.shareus.bot.common.redis.service.RedisService;

import javax.annotation.Resource;

@Aspect
@Slf4j
public class CacheAspect {
	
	@Resource
	private RedisService redisService;
	
	@Around(value = "@annotation(cache)", argNames = "joinPoint, cache")
	public Object around(ProceedingJoinPoint joinPoint, Cache cache) throws Throwable {
		// 拼接redis key，默认为类名+方法名 获取redis值，没有就执行方法 然后缓存结果，有就直接返回 不执行方法
		String key = getKey(joinPoint, cache);
		
		if (redisService.hasKey(key)) {
			return redisService.getCacheObject(key);
		}
		Object o = joinPoint.proceed();
		// 获取方法执行结果
		
		redisService.setCacheObject(key, o, cache.ttl(), cache.unit());
		return o;
	}
	
	/**
	 * 获取key
	 *
	 * @param joinPoint
	 * @param cache
	 *
	 * @return
	 */
	@NotNull
	private static String getKey(ProceedingJoinPoint joinPoint, Cache cache) {
		ExpressionParser parser = new SpelExpressionParser();
		
		String nameKey = StrUtil.isBlank(cache.name())
				? joinPoint.getTarget().getClass().getName()
				: cache.name();
		
		String keyPrefix = nameKey + ":";
		
		return keyPrefix + (StrUtil.isBlank(cache.key())
				? joinPoint.getSignature().getName()
				: cache.key());
	}
	
}
