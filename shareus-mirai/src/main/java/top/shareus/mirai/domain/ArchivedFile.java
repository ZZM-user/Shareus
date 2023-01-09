package top.shareus.mirai.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import top.shareus.common.core.annotation.Excel;
import top.shareus.common.core.web.domain.BaseEntity;

import java.util.Date;


/**
 * 归档文件对象 archived_file
 *
 * @author zhaojl
 * @date 2023-01-09
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArchivedFile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id 为uuid
     */
    @TableField(value = "id")
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
    @TableLogic(value = "0", delval = "1")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "归档日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date archiveDate;
}
