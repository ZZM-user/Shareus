package top.shareus.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 黑名单
 *
 * @TableName black_list
 */
@TableName(value = "black_list")
@Data
public class BlackList implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	@TableId(value = "id")
	private Long id;
	/**
	 * 黑名单用户
	 */
	@TableField(value = "qq_id")
	private String qqId;
	/**
	 * 昵称
	 */
	@TableField(value = "nick_name")
	private String nickName;
	/**
	 * 备注
	 */
	@TableField(value = "remark")
	private String remark;
	/**
	 * 是否删除(0否1是)
	 */
	@TableLogic(value = "0", delval = "1")
	@TableField(value = "del_flag")
	private Integer delFlag;
	/**
	 * 操作人
	 */
	@TableField(value = "create_by")
	private String createBy;
	/**
	 * 操作人QQid
	 */
	@TableField(value = "create_by_id")
	private String createById;
	/**
	 *
	 */
	@TableField(value = "create_time")
	private Date createTime;
}