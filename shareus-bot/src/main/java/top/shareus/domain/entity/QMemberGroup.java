package top.shareus.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.shareus.common.core.annotation.Excel;
import top.shareus.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * QQ成员和群的关系对象 q_member_group
 *
 * @author zhaojl
 * @date 2023-04-02
 */
public class QMemberGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    /**
     * $column.columnComment
     */
    private Long id;
    
    /**
     * QQ成员表id
     */
    @Excel(name = "QQ成员表id")
    private Long qqId;
    
    /**
     * 群组表id
     */
    @Excel(name = "群组表id")
    private Long groupId;
    
    /**
     * 身份 0普通成员 1管理员 2群主
     */
    @Excel(name = "身份 0普通成员 1管理员 2群主")
    private Integer identityType;
    
    /**
     * 群名片
     */
    @Excel(name = "群名片")
    private String nameCard;
    
    /**
     * 排行title
     */
    @Excel(name = "排行title")
    private String rankTitle;
    
    /**
     * 活跃度title
     */
    @Excel(name = "活跃度title")
    private String temperatureTitle;
    
    /**
     * 禁言否 0否 1是
     */
    @Excel(name = "禁言否 0否 1是")
    private Integer muted;
    
    /**
     * 解禁时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "解禁时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date muteTime;
    
    /**
     * 启停标志 0启1停
     */
    @Excel(name = "启停标志 0启1停")
    private Integer enable;
    
    /**
     * 进群时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "进群时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinTime;
    
    /**
     * 最后发言时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后发言时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date speakTime;
    
    /**
     * 删除标记 0未删 1删
     */
    private Integer delFlag;
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("qqId", getQqId())
                .append("groupId", getGroupId())
                .append("identityType", getIdentityType())
                .append("nameCard", getNameCard())
                .append("rankTitle", getRankTitle())
                .append("temperatureTitle", getTemperatureTitle())
                .append("muted", getMuted())
                .append("muteTime", getMuteTime())
                .append("enable", getEnable())
                .append("joinTime", getJoinTime())
                .append("speakTime", getSpeakTime())
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
    
    public Long getQqId() {
        return qqId;
    }
    
    public void setQqId(Long qqId) {
        this.qqId = qqId;
    }
    
    public Long getGroupId() {
        return groupId;
    }
    
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
    public Integer getIdentityType() {
        return identityType;
    }
    
    public void setIdentityType(Integer identityType) {
        this.identityType = identityType;
    }
    
    public String getNameCard() {
        return nameCard;
    }
    
    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }
    
    public String getRankTitle() {
        return rankTitle;
    }
    
    public void setRankTitle(String rankTitle) {
        this.rankTitle = rankTitle;
    }
    
    public String getTemperatureTitle() {
        return temperatureTitle;
    }
    
    public void setTemperatureTitle(String temperatureTitle) {
        this.temperatureTitle = temperatureTitle;
    }
    
    public Integer getMuted() {
        return muted;
    }
    
    public void setMuted(Integer muted) {
        this.muted = muted;
    }
    
    public Date getMuteTime() {
        return muteTime;
    }
    
    public void setMuteTime(Date muteTime) {
        this.muteTime = muteTime;
    }
    
    public Integer getEnable() {
        return enable;
    }
    
    public void setEnable(Integer enable) {
        this.enable = enable;
    }
    
    public Date getJoinTime() {
        return joinTime;
    }
    
    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }
    
    public Date getSpeakTime() {
        return speakTime;
    }
    
    public void setSpeakTime(Date speakTime) {
        this.speakTime = speakTime;
    }
    
    public Integer getDelFlag() {
        return delFlag;
    }
    
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
