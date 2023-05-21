package top.shareus.bot.robot.service;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.shareus.bot.common.domain.ArchivedFile;

import java.util.List;

/**
 * 查询日志服务
 *
 * @author 17602
 * @date 2023/05/21
 */
public interface QueryLogService {
	void recordLog(GroupMessageEvent event, String content, String extract, List<ArchivedFile> result);
	
	void queryLogByBookName(ArchivedFile archivedFile);
}
