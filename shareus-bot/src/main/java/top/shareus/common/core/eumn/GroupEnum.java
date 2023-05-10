package top.shareus.common.core.eumn;

import lombok.Getter;

/**
 * 群组枚举
 *
 * @author zhaojl
 * @date 2023/02/25
 */

public enum GroupEnum {
	
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
	
	@Getter
	private String type;
	
	GroupEnum(String type) {
		this.type = type;
	}
}
