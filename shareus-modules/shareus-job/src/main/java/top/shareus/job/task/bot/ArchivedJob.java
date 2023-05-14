package top.shareus.job.task.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.api.service.RemoteBotJobService;

/**
 * 归档文件任务
 *
 * @author zhaojl
 * @date 2023/01/26
 */
@Component
public class ArchivedJob {
	
	@Autowired
	RemoteBotJobService remoteBotJobService;
	
	
	public void day() {
		remoteBotJobService.day();
	}
	
	public void week() {
		remoteBotJobService.week();
	}
	
	public void month() {
		remoteBotJobService.month();
	}
}
