package top.shareus.bot.robot.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.CharSequenceUtil;
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

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

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
	
	private static final String QIU_WEN_INFO_REGEXP = "^书名[：|:](.*?)\\n作者[：|:](.*?)\\n平台[：|:](.*?)$";
	
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
		redisService.expire(key, expire);
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
		
		return ReUtil.isMatch(QIU_WEN_INFO_REGEXP, content);
	}
	
	/**
	 * 提取书信息
	 *
	 * @param plainText 纯文本
	 *
	 * @return {@code String}
	 */
	@Override
	public List<String> extractBookInfo(PlainText plainText) {
		if (ObjectUtil.isNull(plainText)) {
			return ListUtil.of("");
		}
		
		String content = plainText.getContent();
		
		if (StrUtil.contains(content, "晋江")) {
			return Collections.singletonList("晋江");
		}
		
		// 获取求文信息
		List<String> infoGroupList = ReUtil.getAllGroups(Pattern.compile(QIU_WEN_INFO_REGEXP), content, false);
		log.info("拆分出的信息：{}", String.join(",", infoGroupList));
		
		// 信息不全
		boolean hasEmptyInfo = infoGroupList.stream().anyMatch(String::isEmpty);
		if (hasEmptyInfo) {
			return ListUtil.of("");
		}
		
		// 新规则
		// 书名：静夜思\n作者：李白\n平台：未知
		String bookName = infoGroupList.get(0);
		String author = infoGroupList.get(1);
		bookName = bookName
				.replace("：", "")
				.replace(":", "")
				.replace("《", "")
				.replace("》", "")
				.trim();
		
		bookName = CharSequenceUtil.cleanBlank(bookName);
		
		// 去除 [类型]
		int indexOf = bookName.indexOf("[");
		if (indexOf != - 1) {
			int indexOf1 = bookName.indexOf("]");
			bookName = CharSequenceUtil.replace(bookName, indexOf, indexOf1 + 1, "");
		}
		
		log.info("最终求文拆分结果：{}-{}", bookName, author);
		return ListUtil.of(bookName, author);
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
