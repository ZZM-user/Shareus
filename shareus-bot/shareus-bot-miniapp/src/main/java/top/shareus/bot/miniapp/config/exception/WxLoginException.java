package top.shareus.bot.miniapp.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WxLoginException extends RuntimeException {
	
	private int code = 500;
	
	private String msg;
	
	public WxLoginException(String errorMsg) {
		this.msg = errorMsg;
	}
	
	public WxLoginException(int errorCode, String errorMsg) {
		this.code = errorCode;
		this.msg = errorMsg;
	}
}
