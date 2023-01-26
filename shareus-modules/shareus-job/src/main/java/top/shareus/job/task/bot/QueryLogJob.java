package top.shareus.job.task.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.api.RemoteBotJobService;

/**
 * 查询日志任务
 *
 * @author zhaojl
 * @date 2023/01/26
 */
@Component
public class QueryLogJob {

    @Autowired
    RemoteBotJobService remoteBotJobService;

    public void polling() {
        remoteBotJobService.polling();
    }

    public void feedback() {
        remoteBotJobService.feedback();
    }
}
