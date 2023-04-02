package top.shareus.bot.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import top.shareus.bot.api.factory.RemoteBotFallbackFactory;
import top.shareus.common.core.constant.ServiceNameConstants;
import top.shareus.common.core.domain.R;

/**
 * 远程机器人任务服务
 *
 * @author zhaojl
 * @date 2023/01/26
 */
@FeignClient(contextId = "remoteBotJobService", value = ServiceNameConstants.BOT_SERVICE, fallbackFactory = RemoteBotFallbackFactory.class)
public interface RemoteBotJobService {
    
    /**
     * 轮询求文日志结果
     *
     * @return {@link R}
     */
    @PostMapping(value = "/job/query_log/polling")
    public R polling();
    
    /**
     * 反馈求文情况
     *
     * @return {@link R}
     */
    @PostMapping(value = "/job/query_log/feedback")
    public R feedback();
    
    /**
     * 每天总结
     *
     * @return {@link R}
     */
    @PostMapping(value = "/job/archived_file/day")
    public R day();
    
    /**
     * 每周总结
     *
     * @return {@link R}
     */
    @PostMapping(value = "/job/archived_file/week")
    public R week();
    
    /**
     * 每月总结
     *
     * @return {@link R}
     */
    @PostMapping(value = "/job/archived_file/month")
    public R month();
}
