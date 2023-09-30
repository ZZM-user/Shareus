package top.shareus.bot.robot.job.querylog;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.domain.QueryLog;
import top.shareus.bot.robot.config.BotManager;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.mapper.QueryLogMapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 热搜排名
 *
 * @author zhaojl
 * @date 2023/01/08
 */
@Slf4j
@Component
public class HotQueryRank {
	
	@Autowired
	private QueryLogMapper queryLogMapper;
	
	@Autowired
	private GroupsConfig groupsConfig;
	
	@Scheduled(cron = "0 0 9 ? * 2 ")
	public void execute() {
		int days = 7;
		List<Map<String, QueryLog>> hotQueryRank = queryLogMapper.selectOfNDayHotQueryRank(days);
		StringBuilder builder = new StringBuilder();
		AtomicInteger rank = new AtomicInteger(1);
		
		builder.append("上周热门求文排行榜:\n");
		hotQueryRank.forEach(q -> builder.append(rank.getAndIncrement()).append("、").append(q.get("extract")).append("|").append(q.get("rank")).append("次\n"));
		
		log.info("{}", builder);
		Bot bot = BotManager.getBot();
		groupsConfig.getAdmin().forEach(group -> bot.getGroup(group).sendMessage(builder.toString()));
	}
}
