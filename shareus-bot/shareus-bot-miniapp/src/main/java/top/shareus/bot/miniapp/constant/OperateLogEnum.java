package top.shareus.bot.miniapp.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作日志枚举
 *
 * @author 17602
 */
public class OperateLogEnum {
	
	@Getter
	@AllArgsConstructor
	public enum ActionEnum {
		
		/**
		 * 0. 用户注册
		 */
		REGISTER(0, "注册"),
		
		/**
		 * 1. 用户登录
		 */
		LOGIN(1, "登录"),
		
		/**
		 * 2. 用户修改信息
		 */
		CHANGE_INFO(2, "修改信息"),
		;
		
		private final int value;
		private final String desc;
		
	}
}
