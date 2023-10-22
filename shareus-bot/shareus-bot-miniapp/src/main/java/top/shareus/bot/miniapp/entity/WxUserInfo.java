package top.shareus.bot.miniapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName wx_user_info
 */
@TableName(value = "wx_user_info")
@Data
public class WxUserInfo implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 名称
	 */
	private String nickName;
	/**
	 * 头像
	 */
	private String avatarUrl;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 *
	 */
	private String unionid;
	/**
	 *
	 */
	private String openid;
	/**
	 *
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 *
	 */
	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
}