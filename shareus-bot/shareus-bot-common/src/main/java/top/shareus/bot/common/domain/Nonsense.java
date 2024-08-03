package top.shareus.bot.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 废话表
 *
 * @TableName nonsense
 */
@TableName(value = "nonsense")
@Data
public class Nonsense implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 内容
	 */
	@TableField(value = "content")
	private String content;
	/**
	 * 发送次数
	 */
	@TableField(value = "send_times")
	private Long sendTimes;
	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private Date createTime;
	/**
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private Date updateTime;
}