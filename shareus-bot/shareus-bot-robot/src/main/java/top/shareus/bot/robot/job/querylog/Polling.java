package top.shareus.bot.robot.job.querylog;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.constant.QiuWenConstant;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.domain.QueryLog;
import top.shareus.bot.robot.mapper.QueryLogMapper;
import top.shareus.bot.robot.service.ArchivedFileService;
import top.shareus.bot.robot.service.QueryArchivedResFileService;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 轮询 检查求文是否已完成
 *
 * @author zhaojl
 * @date 2023/01/07
 */
@Slf4j
@Component
public class Polling {
	
	private static QueryLogMapper queryMapper;
	@Autowired
	private QueryLogMapper queryLogMapper;
	@Autowired
	private ArchivedFileService archivedFileService;
	@Autowired
	private QueryArchivedResFileService queryArchivedResFileService;
	
	/**
	 * 完成查询
	 *
	 * @param queryLog     查询日志
	 * @param archivedFile 存档文件
	 */
	public void finishQuery(QueryLog queryLog, ArchivedFile archivedFile) {
		if (ObjectUtil.isNotNull(archivedFile)) {
			finishQuery(queryLog, Lists.newArrayList(archivedFile));
		}
	}
	
	/**
	 * 完成查询
	 *
	 * @param queryLog       查询日志
	 * @param bookInfoByName 图书信息名字
	 */
	private void finishQuery(QueryLog queryLog, List<ArchivedFile> archivedFiles) {
		if (CollUtil.isNotEmpty(archivedFiles)) {
			// 按归档时间排正序
			if (archivedFiles.size() > 1) {
				archivedFiles.stream().sorted(Comparator.comparing(ArchivedFile::getArchiveDate));
			}
			
			ArchivedFile archivedFile = archivedFiles.get(0);
			queryLog.setStatus(0);
			queryLog.setAnswerId(archivedFile.getSenderId());
			queryLog.setResult(JSONUtil.toJsonPrettyStr(archivedFile));
			queryLog.setFinishTime(archivedFile.getArchiveDate());
			queryMapper.updateById(queryLog);
			String key = QiuWenConstant.QIU_WEN_REDIS_KEY + queryLog.getSenderId();
			queryArchivedResFileService.incrTimes(key, QiuWenConstant.getExpireTime());
			log.info("该求文任务完成: " + queryLog);
		}
	}
	
	@PostConstruct
	public void init() {
		queryMapper = queryLogMapper;
	}
	
	public void execute() {
		log.info("开始对求文任务检查……");
		
		List<QueryLog> queryLogs = queryMapper.selectAllUnfinishedQuery();
		
		log.info("待反馈求文任务数量：" + queryLogs.size());
		if (CollUtil.isEmpty(queryLogs)) {
			return;
		}
		
		// 拿拆出来的书名 回查 有的话按早发的人来 并且更新log 增加求文次数
		queryLogs.forEach(queryLog -> {
			String extract = queryLog.getExtract();
			if (StrUtil.isNotBlank(extract)) {
				List<ArchivedFile> bookInfoByName = archivedFileService.findBookInfoByName(extract);
				finishQuery(queryLog, bookInfoByName);
			}
			
			// 超时关闭
			if (overTime(queryLog)) {
				stopQuery(queryLog, "超时未完成，自动关闭！");
			}
		});
		
		log.info("求文任务检查已结束");
	}
	
	/**
	 * 超时
	 *
	 * @param queryLog 查询日志
	 *
	 * @return boolean
	 */
	private boolean overTime(QueryLog queryLog) {
		if (ObjectUtil.isNull(queryLog)) {
			return false;
		}
		
		// 几天没处理了
		long between = DateUtil.between(queryLog.getSendTime(), DateUtil.date(), DateUnit.DAY);
		
		return between >= QiuWenConstant.QIU_WEN_MAX_DAY_WILL_FAIL && 1 == queryLog.getStatus();
	}
	
	/**
	 * 停止查询
	 *
	 * @param queryLog 查询日志
	 * @param cause    原因
	 */
	public void stopQuery(QueryLog queryLog, String cause) {
		queryLog.setStatus(2);
		queryLog.setResult(cause);
		queryLog.setFinishTime(new Date());
		queryMapper.updateById(queryLog);
		String key = QiuWenConstant.QIU_WEN_REDIS_KEY + queryLog.getSenderId();
		queryArchivedResFileService.incrTimes(key, QiuWenConstant.getExpireTime());
		log.info("该求文任务被关闭: " + queryLog);
	}
}
