package top.shareus.bot.robot.job.querylog;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.domain.QueryLog;
import top.shareus.bot.robot.config.BotManager;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.mapper.QueryLogMapper;

import java.util.List;

/**
 * 反馈 每天没有关闭的求文记录
 *
 * @author zhaojl
 * @date 2023/01/08
 */
@Slf4j
@Component
public class Feedback {
	@Autowired
	private QueryLogMapper queryLogMapper;
	
	@Autowired
	private GroupsConfig groupsConfig;
	
	@Scheduled(cron = "0 5 18 * * ? ")
	public void execute() {
		List<QueryLog> queryLogs = queryLogMapper.selectUnfinishedQuery(0);
		if (CollUtil.isEmpty(queryLogs)) {
			log.info("今日求文已完成！");
			return;
		}
		
		senderNotice(queryLogs);
	}
	
	/**
	 * 发送通知
	 *
	 * @param queryLogs 查询日志
	 */
	public void senderNotice(List<QueryLog> queryLogs) {
		Bot bot = BotManager.getBot();
		Group group = bot.getGroup(groupsConfig.getAdmin().get(0));
		queryLogs.forEach(queryLog -> {
			MessageChainBuilder builder = new MessageChainBuilder();
			builder.add("近三天未完成的求文：");
			builder.add("\n求文内容: \n\n" + queryLog.getContent());
			builder.add("\n\n求文QQ: " + queryLog.getSenderId());
			builder.add("\n求文者: " + queryLog.getSenderName());
			builder.add("\n时间: " + DateUtil.formatDateTime(queryLog.getSendTime()));
			group.sendMessage(builder.build());
		});
	}
}
