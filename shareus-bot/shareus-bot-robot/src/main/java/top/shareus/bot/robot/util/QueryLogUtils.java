package top.shareus.bot.robot.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.domain.QueryLog;
import top.shareus.bot.robot.job.querylog.Polling;
import top.shareus.bot.robot.mapper.QueryLogMapper;

import java.util.Date;
import java.util.List;

/**
 * 查询日志 工具类
 *
 * @author zhaojl
 * @date 2023/01/07
 */
@Slf4j
public class QueryLogUtils {
	
	private static QueryLogMapper queryLogMapper = SpringUtil.getBean(QueryLogMapper.class);
	
	/**
	 * 记录日志
	 *
	 * @param event   事件
	 * @param content 内容
	 * @param extract 提取
	 * @param result  结果
	 */
	public static void recordLog(GroupMessageEvent event, String content, String extract, List<ArchivedFile> result) {
		boolean hasResult = CollUtil.isNotEmpty(result);
		QueryLog queryLog = new QueryLog();
		
		queryLog.setSenderName(event.getSenderName());
		queryLog.setSenderId(event.getSender().getId());
		queryLog.setContent(content);
		queryLog.setExtract(extract);
		queryLog.setStatus(1);
		queryLog.setSendTime(DateUtil.date(event.getTime() * 1000L));
		
		// 如果直接成功了
		if (hasResult) {
			queryLog.setStatus(0);
			queryLog.setAnswerId(0L);
			queryLog.setResult(JSONUtil.toJsonPrettyStr(result));
			queryLog.setFinishTime(new Date());
		}
		
		queryLogMapper.insert(queryLog);
		log.info("求文记录：" + queryLog);
	}
	
	/**
	 * 查询日志 书名
	 *
	 * @param bookName 书名
	 */
	public static void queryLogByBookName(ArchivedFile archivedFile) {
		if (ObjectUtil.isNull(archivedFile)) {
			return;
		}
		List<QueryLog> queryLogs = queryLogMapper.queryLogByBookName(archivedFile.getName());
		
		if (CollUtil.isEmpty(queryLogs)) {
			return;
		}
		
		queryLogs.forEach(queryLog -> {
			Polling.finishQuery(queryLog, archivedFile);
		});
	}
	
}
