package top.shareus.bot.robot.job.archived;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
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
 * 月
 *
 * @author zhaojl
 * @date 2022/11/27
 */
@Slf4j
@Component
public class Month {
	
	@Autowired
	private ArchivedFileMapper archivedFileMapper;
	@Autowired
	private GroupsConfig groupsConfig;
	
	//	@Scheduled(cron = "0 0 22 28-31 * ?")
	@Scheduled(cron = "0 0 8 15 * ?")
	public void execute() {
		// 每月发送统计信息
		int lastMonthDays = DateUtil.lastMonth().getLastDayOfMonth();
		int thisMonthDays = DateTime.now().getLastDayOfMonth();
		int computedDays = lastMonthDays - 15 + thisMonthDays;
		
		Integer hasArchived = archivedFileMapper.countByDaysOfBefore(computedDays);
		List<ShareFileStar> stars = archivedFileMapper.computedFileStar(computedDays);
		
		Assert.notNull(hasArchived, "获取本月归档信息失败!");
		Assert.notNull(stars, "获取本月分享之星失败!");
		
		MessageChainBuilder builder = new MessageChainBuilder();
		builder.append(Character.highSurrogate(DateUtil.lastMonth().monthBaseOne()));
		builder.append("月总结：");
		builder.add("\n本月资源群归档文件数量：" + hasArchived);
		builder.add("----------------------");
		builder.add("\n本月分享之星：");
		stars.forEach(star -> {
			builder.add(new At(star.getSenderId()));
			builder.add("\nTA本月为我们分享了" + star.getTimes() + "次文件");
			// 创建一个数值格式化对象
			NumberFormat numberFormat = NumberFormat.getInstance();
			// 设置精确到小数点后2位
			numberFormat.setMaximumFractionDigits(2);
			builder.add("\n占本月的 " + numberFormat.format((float) star.getTimes() / (float) hasArchived * 100) + "%");
			builder.add("----------------------");
		});
		
		Bot bot = BotManager.getBot();
		Group group = bot.getGroupOrFail(groupsConfig.getTest().get(0));
		log.info(builder.build().toString());
		group.sendMessage(builder.build());
		
		bot.getGroup(groupsConfig.getAdmin().get(0)).sendMessage(builder.build());
	}
}
