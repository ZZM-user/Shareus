package top.shareus.bot.miniapp.service;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.redis.service.RedisService;
import top.shareus.bot.miniapp.constant.SecurityConstants;
import top.shareus.bot.miniapp.entity.WxUserInfo;
import top.shareus.bot.miniapp.mapper.WxUserInfoMapper;
import top.shareus.common.core.exception.mirai.bot.BotException;
import top.shareus.common.security.utils.SecurityUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * token服务
 *
 * @author 17602
 */
@Slf4j
@Service
public class MiniAppTokenService {
	private static final String TOKEN_KEY = "miniapp_login_token:";
	private static final String SIGN = "hpL%d+W*%-*lrspdls;ei5n29854n02mvoo15-786%32jds3453-!cM-66_b^y^W_P*w";
	private static final Long TOKEN_EXPIRE = 1L;
	
	@Resource
	private RedisService redisService;
	@Resource
	private WxUserInfoMapper wxUserInfoMapper;
	
	/**
	 * 生成并存入token
	 *
	 * @param userId
	 *
	 * @return
	 */
	public String generateAndSaveToken(Long userId) {
		String token = generateToken(userId);
		saveToken(token, userId);
		return token;
	}
	
	/**
	 * 生成token
	 *
	 * @param userId
	 *
	 * @return
	 */
	public String generateToken(Long userId) {
		HashMap<String, Object> map = new HashMap(2);
		map.put("user_id", userId);
		map.put("time", System.currentTimeMillis());
		return JWTUtil.createToken(map, SIGN.getBytes());
	}
	
	/**
	 * 存入token
	 *
	 * @param token
	 * @param userId
	 */
	public void saveToken(String token, Long userId) {
		WxUserInfo wxUser = wxUserInfoMapper.selectById(userId);
		redisService.setCacheObject(generateKey(token), JSON.toJSONString(wxUser), TOKEN_EXPIRE, TimeUnit.HOURS);
	}
	
	/**
	 * 生成key
	 *
	 * @param token
	 *
	 * @return
	 */
	public String generateKey(String token) {
		return TOKEN_KEY + token;
	}
	
	/**
	 * 获取当前登录用户的id
	 *
	 * @return
	 */
	public Long getCurrentUserId() {
		WxUserInfo currentUser = getCurrentUser();
		if (ObjectUtil.isNull(currentUser)) {
			throw new BotException("获取用户信息失败！", HttpStatus.HTTP_FORBIDDEN);
		}
		return currentUser.getId();
	}
	
	/**
	 * 查询当前用户信息
	 *
	 * @return
	 */
	public WxUserInfo getCurrentUser() {
		return getUserInfoByToken(getToken());
	}
	
	/**
	 * 根据token查询用户信息
	 *
	 * @param token
	 *
	 * @return
	 */
	public WxUserInfo getUserInfoByToken(String token) {
		if (CharSequenceUtil.isEmpty(token)) {
			throw new BotException("token不能为空", HttpStatus.HTTP_FORBIDDEN);
		}
		
		token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
		try {
			if (! JWTUtil.verify(token, SIGN.getBytes())) {
				throw new BotException("非法token：" + token, HttpStatus.HTTP_FORBIDDEN);
			}
		} catch (JWTException e) {
			log.error("非法token", e);
			throw new BotException("非法token：" + token, HttpStatus.HTTP_FORBIDDEN);
		}
		
		
		Object cacheObject = redisService.getCacheObject(generateKey(token));
		if (ObjectUtil.isNotNull(cacheObject)) {
			return JSON.parseObject(cacheObject.toString(), WxUserInfo.class);
		}
		throw new BotException("token已过期", HttpStatus.HTTP_FORBIDDEN);
	}
	
	/**
	 * 获取requests请求头中的token
	 *
	 * @return token
	 */
	public String getToken() {
		return SecurityUtils.getToken();
	}
}
