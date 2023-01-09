package top.shareus.mirai.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import top.shareus.common.core.annotation.Excel;
import top.shareus.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * 机器人对象 archived_file
 *
 * @author zhaojl
 * @date 2023-01-09
 */
public class ArchivedFile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id 为uuid
     */
    private String id;

    /**
     * 文件名
     */
    @Excel(name = "文件名")
    private String name;

    /**
     * 发件人QQ
     */
    @Excel(name = "发件人QQ")
    private Long senderId;

    /**
     * 长度
     */
    @Excel(name = "长度")
    private Long size;

    /**
     * MD5
     */
    @Excel(name = "MD5")
    private String md5;

    /**
     * 是否展示(0是 1否)
     */
    @Excel(name = "是否展示(0是 1否)")
    private Integer enabled;

    /**
     * 是否删除(0否 1是)
     */
    private Integer delFlag;

    /**
     * 源路径
     */
    @Excel(name = "源路径")
    private String originUrl;

    /**
     * 存档路径
     */
    @Excel(name = "存档路径")
    private String archiveUrl;

    /**
     * 归档日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "归档日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date archiveDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("senderId", getSenderId())
                .append("size", getSize())
                .append("md5", getMd5())
                .append("enabled", getEnabled())
                .append("delFlag", getDelFlag())
                .append("originUrl", getOriginUrl())
                .append("archiveUrl", getArchiveUrl())
                .append("archiveDate", getArchiveDate())
                .toString();
    }
}
