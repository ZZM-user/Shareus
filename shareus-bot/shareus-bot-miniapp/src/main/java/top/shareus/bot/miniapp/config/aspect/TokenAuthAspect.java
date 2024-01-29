package top.shareus.bot.miniapp.config.aspect;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import top.shareus.bot.miniapp.entity.WxUserInfo;
import top.shareus.bot.miniapp.service.MiniAppTokenService;
import top.shareus.common.core.exception.mirai.bot.BotException;

import javax.annotation.Resource;

/**
 * token验证切面
 *
 * @author zhaojl
 * @date 2024/01/17
 */
@Aspect
@Component
public class TokenAuthAspect {
	
	@Resource
	private MiniAppTokenService tokenService;
	
	@Pointcut("@annotation(top.shareus.bot.miniapp.config.aspect.TokenAuth)")
	public void tokenAuthPointcut() {
	}
	
	@Before("tokenAuthPointcut()")
	public void authorizeToken() {
		// 获取当前请求的Token
		String token = tokenService.getToken();
		
		if (CharSequenceUtil.isEmpty(token)) {
			throw new BotException("请登录后使用……", HttpStatus.HTTP_FORBIDDEN);
		}
		
		WxUserInfo wxUser = tokenService.getUserInfoByToken(token);
		if (ObjectUtil.isNull(wxUser)) {
			throw new BotException("token已过期，请重新登录后使用……", HttpStatus.HTTP_FORBIDDEN);
		}
	}
}
