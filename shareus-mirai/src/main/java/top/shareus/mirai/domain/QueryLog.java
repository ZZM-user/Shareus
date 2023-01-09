package top.shareus.mirai.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.shareus.common.core.annotation.Excel;
import top.shareus.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * 求文日志对象 query_log
 *
 * @author zhaojl
 * @date 2023-01-09
 */
public class QueryLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 发送人昵称
     */
    @Excel(name = "发送人昵称")
    private String senderName;

    /**
     * 发送人QQ
     */
    @Excel(name = "发送人QQ")
    private Long senderId;

    /**
     * 内容
     */
    @Excel(name = "内容")
    private String content;

    /**
     * 抽取内容
     */
    @Excel(name = "抽取内容")
    private String extract;

    /**
     * 状态(0成功 1等待 2失败)
     */
    @Excel(name = "状态(0成功 1等待 2失败)")
    private Integer status;

    /**
     * 求文反馈者(0 机器人/其他人QQ)
     */
    @Excel(name = "求文反馈者(0 机器人/其他人QQ)")
    private Long answerId;

    /**
     * 反馈结果
     */
    @Excel(name = "反馈结果")
    private String result;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /**
     * 出结果时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出结果时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date finishTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("senderName", getSenderName())
                .append("senderId", getSenderId())
                .append("content", getContent())
                .append("extract", getExtract())
                .append("status", getStatus())
                .append("answerId", getAnswerId())
                .append("result", getResult())
                .append("sendTime", getSendTime())
                .append("finishTime", getFinishTime())
                .toString();
    }
}
