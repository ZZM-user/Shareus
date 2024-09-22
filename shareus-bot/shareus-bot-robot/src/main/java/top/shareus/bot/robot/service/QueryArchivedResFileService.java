package top.shareus.bot.robot.service;

import net.mamoe.mirai.message.data.PlainText;

import java.util.List;

/**
 * 查询归档res文件服务
 *
 * @author 17602
 * @date 2023/05/21
 */
public interface QueryArchivedResFileService {
	boolean checkWarring(Long senderId, String nickName);
	
	int getTimes(String key);
	
	void checkTemplateError(long senderId, String senderName);
	
	int incrTimes(String key, long expire);
	
	boolean isQiuWen(PlainText plainText);
	
	List<String> extractBookInfo(PlainText plainText);
}
