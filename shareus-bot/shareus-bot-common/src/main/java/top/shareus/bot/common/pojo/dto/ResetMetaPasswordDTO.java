package top.shareus.bot.common.pojo.dto;


import lombok.Data;

/**
 * 重置资源元数据文件密码
 *
 * @author 17602
 */
@Data
public class ResetMetaPasswordDTO {
	
	/**
	 * 指定密码 没有则随机生成
	 */
	private String password;
	
	/**
	 * 是否取消密码
	 * 是：直接无视所有密码选项
	 */
	private Boolean cancelPassword;
	
	/**
	 * 是否通知管理组
	 */
	private Boolean notifyAdminGroup;
	
	/**
	 * 是否通知资源组
	 */
	private Boolean notifyResourceGroup;
	
}
