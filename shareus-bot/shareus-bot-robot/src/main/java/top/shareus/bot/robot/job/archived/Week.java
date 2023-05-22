package top.shareus.bot.robot.job.archived;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.domain.ShareFileStar;
import top.shareus.bot.robot.config.BotManager;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.mapper.ArchivedFileMapper;

import java.text.NumberFormat;
import java.util.List;

/**
 * 周
 *
 * @author zhaojl
 * @date 2022/11/27
 */
@Slf4j
@Component
public class Week {
	
	@Autowired
	private ArchivedFileMapper archivedFileMapper;
	@Autowired
	private GroupsConfig groupsConfig;
	
	@Scheduled(cron = "0 22 * * 7")
	public void execute() {
		// 每周发送统计信息
		Integer hasArchived = archivedFileMapper.countByDaysOfBefore(7);
		List<ShareFileStar> stars = archivedFileMapper.computedFileStar(7);
		
		Assert.notNull(hasArchived, "获取本周归档信息失败!");
		Assert.notNull(stars, "获取本周分享之星失败!");
		
		ShareFileStar star = stars.get(0);
		MessageChainBuilder builder = new MessageChainBuilder();
		builder.append("本周总结：");
		builder.add("\n本周资源群归档文件数量：" + hasArchived);
		builder.add("\n本周分享之星：");
		builder.add(new At(star.getSenderId()));
		builder.add("\nTA本周为我们分享了 " + star.getTimes() + " 次文件");
		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);
		builder.add("\n占本周的 " + numberFormat.format((float) star.getTimes() / (float) hasArchived * 100) + "%");
		
		Bot bot = BotManager.getBot();
		Group group = bot.getGroupOrFail(groupsConfig.getTest().get(0));
		log.info(builder.build().toString());
		group.sendMessage(builder.build());
		
		bot.getGroup(groupsConfig.getAdmin().get(0)).sendMessage(builder.build());
	}
}
