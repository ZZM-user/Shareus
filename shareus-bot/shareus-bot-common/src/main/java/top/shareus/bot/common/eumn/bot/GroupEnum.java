package top.shareus.bot.common.eumn.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 群组枚举
 *
 * @author zhaojl
 * @date 2023/02/25
 */

@Getter
@AllArgsConstructor
public enum GroupEnum {
	
	/**
	 *
	 */
	ALL("all"),
	
	/**
	 * 管理组
	 */
	ADMIN("admin"),
	
	/**
	 * 资源组
	 */
	RES("res"),
	
	/**
	 * 聊天组
	 */
	CHAT("chat"),
	
	/**
	 * GPT组
	 */
	TEST("test"),
	
	GPT("gpt");
	
	private final String type;
	
}
