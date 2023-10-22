package top.shareus.bot.miniapp.config.exception;

import com.alibaba.nacos.shaded.io.grpc.netty.shaded.io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.shareus.common.core.domain.R;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * 处理登录异常
	 *
	 * @param req
	 * @param e
	 *
	 * @return
	 */
	@ExceptionHandler(value = WxLoginException.class)
	public R bizExceptionHandler(HttpServletRequest req, WxLoginException e) {
		log.error("发生微信登录异常！", e);
		return R.fail(e.getCode(), e.getMsg());
	}
	
	/**
	 * 处理其他异常
	 *
	 * @param req
	 * @param e
	 *
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public R exceptionHandler(HttpServletRequest req, Exception e) {
		log.error("未知异常！原因是:", e);
		return R.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR);
	}
}