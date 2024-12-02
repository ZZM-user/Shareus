package top.shareus.bot.common.domain;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.shareus.common.core.annotation.Excel;
import top.shareus.common.core.web.domain.BaseEntity;

/**
 * QQ成员对象 q_member
 *
 * @author zhaojl
 * @date 2023-04-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QMember extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/**
	 * $column.columnComment
	 */
	private Long id;
	
	/**
	 * QQ号码
	 */
	@Excel(name = "QQ号码")
	private Long qq;
	
	/**
	 * 昵称
	 */
	@Excel(name = "昵称")
	private String nickName;
	
	/**
	 * 头像
	 */
	@Excel(name = "头像")
	private String avatarUrl;
	
	/**
	 * 特殊头衔
	 */
	@Excel(name = "特殊头衔")
	private String specialTitle;
	
	/**
	 * 删除标记 0未删；1删
	 */
	@TableLogic(value = "0", delval = "1")
	private Integer delFlag;
}
