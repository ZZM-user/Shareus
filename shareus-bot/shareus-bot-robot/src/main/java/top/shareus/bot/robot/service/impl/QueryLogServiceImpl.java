package top.shareus.bot.robot.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.domain.QueryLog;
import top.shareus.bot.robot.job.querylog.Polling;
import top.shareus.bot.robot.mapper.QueryLogMapper;
import top.shareus.bot.robot.service.QueryLogService;

import java.util.Date;
import java.util.List;

/**
 * 查询日志服务impl
 *
 * @author 17602
 * @date 2023/05/21
 */
@Slf4j
@Service
public class QueryLogServiceImpl implements QueryLogService {
	
	@Autowired
	private static QueryLogMapper queryLogMapper;
	@Autowired
	private Polling polling;
	
	/**
	 * 记录日志
	 *
	 * @param event   事件
	 * @param content 内容
	 * @param extract 提取
	 * @param result  结果
	 */
	@Override
	public void recordLog(GroupMessageEvent event, String content, String extract, List<ArchivedFile> result) {
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
	@Override
	public void queryLogByBookName(ArchivedFile archivedFile) {
		if (ObjectUtil.isNull(archivedFile)) {
			return;
		}
		List<QueryLog> queryLogs = queryLogMapper.queryLogByBookName(archivedFile.getName());
		
		if (CollUtil.isEmpty(queryLogs)) {
			return;
		}
		
		queryLogs.forEach(queryLog -> {
			polling.finishQuery(queryLog, archivedFile);
		});
	}
}
