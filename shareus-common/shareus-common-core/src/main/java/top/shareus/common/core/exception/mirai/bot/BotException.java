package top.shareus.common.core.exception.mirai.bot;

/**
 * 机器人异常
 *
 * @author zhaojl
 * @date 2023/01/22
 */
public class BotException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public BotException() {
	}
	
	public BotException(String msg) {
		super(msg);
	}
	
	public BotException(String msg, Exception e) {
		super(msg, e);
	}
	
	public BotException(String msg, int code) {
	}
}
