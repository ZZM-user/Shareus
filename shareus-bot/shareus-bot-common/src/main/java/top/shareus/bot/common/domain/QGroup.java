package top.shareus.bot.common.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.shareus.common.core.annotation.Excel;
import top.shareus.common.core.web.domain.BaseEntity;

/**
 * QQ群组对象 q_group
 *
 * @author zhaojl
 * @date 2023-04-02
 */
public class QGroup extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/**
	 * $column.columnComment
	 */
	private Long id;
	
	/**
	 * 群id
	 */
	@Excel(name = "群id")
	private Long groupId;
	
	/**
	 * 群名
	 */
	@Excel(name = "群名")
	private String groupName;
	
	/**
	 * 头像
	 */
	@Excel(name = "头像")
	private String avatarUrl;
	
	/**
	 * 群组类型(0管理群；1聊天群；2资源群；3审核群；5活动群)
	 */
	@Excel(name = "群组类型(0管理群；1聊天群；2资源群；3审核群；5活动群)")
	private Integer type;
	
	/**
	 * 删除标记 0未删 1删
	 */
	private Integer delFlag;
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("groupId", getGroupId())
				.append("groupName", getGroupName())
				.append("avatarUrl", getAvatarUrl())
				.append("type", getType())
				.append("remark", getRemark())
				.append("delFlag", getDelFlag())
				.append("createBy", getCreateBy())
				.append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime())
				.toString();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getGroupId() {
		return groupId;
	}
	
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getAvatarUrl() {
		return avatarUrl;
	}
	
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getDelFlag() {
		return delFlag;
	}
	
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
}
