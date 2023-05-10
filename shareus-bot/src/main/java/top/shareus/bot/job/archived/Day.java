package top.shareus.bot.job.archived;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.config.BotManager;
import top.shareus.bot.config.GroupsConfig;
import top.shareus.bot.mapper.ArchivedFileMapper;
import top.shareus.bot.mapper.QueryLogMapper;
import top.shareus.domain.entity.QueryLog;

import java.util.List;

/**
 * 一天
 *
 * @author zhaojl
 * @date 2022/11/27
 */
@Slf4j
@Component
public class Day {
	
	@Autowired
	private ArchivedFileMapper archivedFileMapper;
	
	@Autowired
	private QueryLogMapper queryLogMapper;
	
	@Autowired
	private GroupsConfig groupsConfig;
	
	public void execute() {
		// 每天发送统计信息
		Integer hasArchived = archivedFileMapper.countByYesterday();
		
		Integer hasQueryLogs = queryLogMapper.countByYesterday();
		List<QueryLog> unfinishedQuery = queryLogMapper.selectUnfinishedQuery(1);
		
		MessageChainBuilder builder = new MessageChainBuilder();
		builder.add("昨日资源群归档文件数量：" + hasArchived);
		builder.add("\n昨日资源群求文数量：" + hasQueryLogs);
		builder.add("\n资源群未完成的求文数量：" + unfinishedQuery.size());
		
		Bot bot = BotManager.getBot();
		Group group = bot.getGroupOrFail(groupsConfig.getTest().get(0));
		log.info(builder.build().toString());
		group.sendMessage(builder.build());
		
		bot.getGroup(groupsConfig.getAdmin().get(0)).sendMessage(builder.build());
	}
}
