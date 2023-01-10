package top.shareus.mirai.vo;

import lombok.Data;

import java.util.List;

/**
 * 批改变状态
 *
 * @author zhaojl
 * @date 2023/01/10
 */
@Data
public class BatchChangeStatusVO {

    private List<Long> ids;

    private List<String> stringIds;
    private Integer status;
}
