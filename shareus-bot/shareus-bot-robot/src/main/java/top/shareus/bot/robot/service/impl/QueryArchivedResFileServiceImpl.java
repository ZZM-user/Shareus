package top.shareus.bot.robot.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.constant.QiuWenConstant;
import top.shareus.bot.common.redis.service.RedisService;
import top.shareus.bot.robot.config.BotManager;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.service.QueryArchivedResFileService;
import top.shareus.bot.robot.util.MuteUtils;

/**
 * 查询归档res impl文件服务
 *
 * @author 17602
 * @date 2023/05/21
 */
@Slf4j
@Service
public class QueryArchivedResFileServiceImpl implements QueryArchivedResFileService {
	
	@Autowired
	private RedisService redisService;
	@Autowired
	private GroupsConfig groupsConfig;
	
	/**
	 * 检查 求文次数是否正常
	 *
	 * @param event 事件
	 *
	 * @return boolean
	 */
	@Override
	public boolean checkWarring(Long senderId, String nickName) {
		Bot bot = BotManager.getBot();
		Group adminGroup = bot.getGroup(groupsConfig.getAdmin().get(0));
		
		NormalMember normalMember = adminGroup.getMembers().get(senderId);
		if (ObjectUtil.isNotNull(normalMember)) {
			log.info("忽略管理员的次数限制：{}", nickName);
			return false;
		}
		
		String key = QiuWenConstant.QIU_WEN_REDIS_KEY + senderId;
		int times = this.getTimes(key);
		
		if (times > QiuWenConstant.MAX_TIMES_OF_DAY) {
			adminGroup.sendMessage("请注意 \n[" + senderId + nickName + "]\n该用户今日已求文第 " + times + " 次");
			MuteUtils.mute(adminGroup, senderId, QiuWenConstant.getExpireTime());
			return true;
		}
		
		return false;
	}
	
	/**
	 * 获取求文次数
	 *
	 * @return int
	 */
	@Override
	public int getTimes(String key) {
		Object times = redisService.getCacheObject(key);
		return ObjectUtil.isNotNull(times) ? (int) times : 0;
	}
	
	/**
	 * 检查模板错误
	 *
	 * @param senderId   发件人id
	 * @param senderName 发送者名字
	 */
	@Override
	public void checkTemplateError(long senderId, String senderName) {
		String key = QiuWenConstant.ERROR_QIU_WEN_TEMPLATE_REDIS_KEY + senderId;
		int times = this.incrTimes(key, QiuWenConstant.getExpireTimeOfErrorTemplate());
		
		if (times >= QiuWenConstant.ERROR_TEMPLATE_MAX_TIMES_OF_WEEK) {
			Bot bot = BotManager.getBot();
			Group group = bot.getGroup(groupsConfig.getAdmin().get(0));
			group.sendMessage("请注意 \n发现 【" + senderName + "】(" + senderId + ")]\n该用户本周第 " + times + " 次，求文规范错误");
			MuteUtils.mute(group, senderId, QiuWenConstant.getExpireTimeOfErrorTemplate());
		}
	}
	
	/**
	 * 增加次数
	 */
	@Override
	public int incrTimes(String key, long expire) {
		redisService.increment(key, 1L);
		return redisService.getCacheObject(key);
	}
	
	/**
	 * 是求文
	 *
	 * @param plainText 纯文本
	 *
	 * @return boolean
	 */
	@Override
	public boolean isQiuWen(PlainText plainText) {
		if (ObjectUtil.isNull(plainText)) {
			return false;
		}
		
		String content = plainText.getContent();
		if (content.length() > 50) {
			log.info("这哪是求文啊，发公告呢吧…… " + content.length());
			return false;
		}
		
		return ReUtil.contains("(书名)", content) && ReUtil.contains("(作者)", content) && ReUtil.contains("(平台)", content);
	}
	
	/**
	 * 提取书信息
	 *
	 * @param plainText 纯文本
	 *
	 * @return {@code String}
	 */
	@Override
	public String extractBookInfo(PlainText plainText) {
		if (ObjectUtil.isNull(plainText)) {
			return "";
		}
		
		String content = plainText.getContent();
		if (! content.startsWith("书名")) {
			return "";
		}
		
		if (StrUtil.contains(content, "晋江")) {
			return "晋江";
		}
		
		// 新规则
		// 书名：静夜思\n作者：李白\n平台：未知
		String[] split = content.split("\n");
		String result = StrUtil.removePrefix(split[0], "书名");
		result = result
				.replace("：", "")
				.replace(":", "")
				.replace("《", "")
				.replace("》", "")
				.trim();
		result = StrUtil.cleanBlank(result);
		
		// 去除 [类型]
		int indexOf = result.indexOf("[");
		if (indexOf != - 1) {
			int indexOf1 = result.indexOf("]");
			result = StrUtil.replace(result, indexOf, indexOf1 + 1, "");
		}
		
		// 太长折半
		if (result.length() > 30) {
			result = result.substring(0, result.length() / 2);
		}
		
		log.info("求文拆分结果：" + result);
		
		return result;
	}
	
	/**
	 * 消减次数
	 */
	public void decrTimes(String key, long expire) {
		int oldValue = redisService.getCacheObject(key);
		if (ObjectUtil.isNotNull(oldValue)) {
			redisService.increment(key, - 1L);
		} else {
			redisService.set(key, "0", expire);
		}
	}
}
