package top.shareus.bot.common.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 黑名单 保存 dto
 *
 * @author 17602
 * @date 2023/06/11
 */
@Data
public class BlackListSaveDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String qqId;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 操作人
	 */
	private String createBy;
	
	/**
	 * 操作人QQid
	 */
	private String createById;
}
