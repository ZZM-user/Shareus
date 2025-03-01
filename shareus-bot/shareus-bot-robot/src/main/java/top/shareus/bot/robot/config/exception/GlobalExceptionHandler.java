package top.shareus.bot.robot.config.exception;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.shareus.bot.robot.util.PushPlusUtil;

@RestControllerAdvice
class GlobalExceptionHandler {
	
	@ExceptionHandler(IllegalStateException.class)
	ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
		String eMessage = e.getMessage();
		if (StrUtil.isNotBlank(eMessage) && eMessage.contains("QQSecuritySign")) {
			PushPlusUtil.pushMsg("机器人被风控了", "请稍后再试！");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("机器人被风控了，请稍后再试！");
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(eMessage);
	}
	
}
