package top.shareus.bot.robot.util;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.redis.service.RedisService;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class QQInfoUtil {
	
	/**
	 * 获取QQ昵称
	 */
	private static final String API = "https://api.szfx.top/qq/info/?qq=";
	
	
	@Resource
	private RedisService redisService;
	
	/**
	 * 获取QQ信息
	 *
	 * @param qq
	 *
	 * @return
	 */
	public QQInfo getInfo(String qq) {
		if (StrUtil.isBlank(qq)) {
			return null;
		}
		
		String redisKey = "QQINFO2:" + qq;
		Object object = redisService.getCacheObject(redisKey);
		if (object != null) {
			return (QQInfo) object;
		}
		
		String resp = HttpUtil.get(API + qq);
		log.info("QQ信息：{}", resp);
		Map map = JSONUtil.toBean(resp, Map.class);
		QQInfo info = new QQInfo();
		if (ObjectUtil.isNotNull(map)) {
			info.setAvatar((String) map.get("headimg"));
			info.setName((String) map.get("nickname"));
		}
		
		redisService.setCacheObject(redisKey, info, 60 * 60 * 10L, TimeUnit.SECONDS);
		return info;
	}
	
	@Data
	public static class QQInfo {
		private Long qq;
		private String name;
		private String avatar;
	}
}
