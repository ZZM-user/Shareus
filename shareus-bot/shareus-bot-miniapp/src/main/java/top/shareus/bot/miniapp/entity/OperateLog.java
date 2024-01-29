package top.shareus.bot.miniapp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName operate_log
 */
@Builder
@TableName(value = "operate_log")
@Data
public class OperateLog implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	@TableId(value = "id")
	private Long id;
	/**
	 *
	 */
	@TableField(value = "user_id")
	private Long userId;
	/**
	 * 动作：0-注册 1-登录
	 */
	@TableField(value = "action")
	private Integer action;
	/**
	 *
	 */
	@TableField(value = "remark")
	private String remark;
	/**
	 *
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
}