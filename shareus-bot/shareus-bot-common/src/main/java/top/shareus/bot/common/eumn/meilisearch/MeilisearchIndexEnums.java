package top.shareus.bot.common.eumn.meilisearch;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Meilisearch索引枚举
 */
@Getter
@AllArgsConstructor
public enum MeilisearchIndexEnums {
	
	/**
	 * 归档文件索引
	 */
	ARCHIVED_FILE("archivedFile", "id", "归档文件索引");
	
	private String index;
	private String primaryKey;
	private String desc;
}
