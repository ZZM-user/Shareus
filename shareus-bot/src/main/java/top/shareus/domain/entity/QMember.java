package top.shareus.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.shareus.common.core.annotation.Excel;
import top.shareus.common.core.web.domain.BaseEntity;

/**
 * QQ成员对象 q_member
 *
 * @author zhaojl
 * @date 2023-04-02
 */
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
    private String avatrarUrl;
    
    /**
     * 特殊头衔
     */
    @Excel(name = "特殊头衔")
    private String specialTitle;
    
    /**
     * 删除标记 0未删；1删
     */
    private Integer delFlag;
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("qq", getQq())
                .append("nickName", getNickName())
                .append("avatrarUrl", getAvatrarUrl())
                .append("specialTitle", getSpecialTitle())
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
    
    public Long getQq() {
        return qq;
    }
    
    public void setQq(Long qq) {
        this.qq = qq;
    }
    
    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public String getAvatrarUrl() {
        return avatrarUrl;
    }
    
    public void setAvatrarUrl(String avatrarUrl) {
        this.avatrarUrl = avatrarUrl;
    }
    
    public String getSpecialTitle() {
        return specialTitle;
    }
    
    public void setSpecialTitle(String specialTitle) {
        this.specialTitle = specialTitle;
    }
    
    public Integer getDelFlag() {
        return delFlag;
    }
    
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
