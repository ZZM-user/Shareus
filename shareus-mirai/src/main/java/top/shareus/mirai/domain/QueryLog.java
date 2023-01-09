package top.shareus.mirai.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import top.shareus.common.core.annotation.Excel;
import top.shareus.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * 求文日志对象 query_log
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
public class QueryLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableField(value = "id")
    private String id;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 出结果时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出结果时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
}
