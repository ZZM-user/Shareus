package top.shareus.bot.robot.job;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.domain.Nonsense;
import top.shareus.bot.robot.config.BotManager;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.service.NonsenseService;
import top.shareus.bot.robot.util.PushPlusUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SendNonsenseJob {
	
	@Resource
	private NonsenseService nonsenseService;
	
	@Resource
	private GroupsConfig groupsConfig;
	
	@Scheduled(cron = "0 0 9,11,14,18,20,23 * * *")
	// @Scheduled(cron = "0 * * * * *")
	public void sendNonsense() {
		log.info("定时任务：发送随机内容");
		Bot bot = BotManager.getBot();
		
		groupsConfig.getChat().forEach(group -> {
			Group botGroup = bot.getGroup(group);
			if (ObjectUtil.isNull(botGroup)) {
				return;
			}
			Nonsense nonsense = getNonsense();
			if (ObjectUtil.isNull(nonsense) || StrUtil.isBlank(nonsense.getContent())) {
				log.error("随机内容获取失败，没得发");
				return;
			}
			String content = nonsense.getContent();
			log.info("即将发送的随机内容：{}", content);
			MessageChainBuilder chainBuilder = new MessageChainBuilder();
			chainBuilder.append(new At(3039313129L));
			chainBuilder.append(" ");
			chainBuilder.append(content);
			try {
				botGroup.sendMessage(chainBuilder.build());
			} catch (Exception e) {
				log.error("发送内容失败", e);
				PushPlusUtil.pushMsg("机器人被风控了", "请稍后再试！");
			}
		});
	}
	
	/**
	 * 获取随机内容
	 *
	 * @return
	 */
	private Nonsense getNonsense() {
		int maxRequestTimes = 50;
		Nonsense nonsense = null;
		try {
			String requestContent;
			String nonsenseContent = null;
			for (int i = 0; i < maxRequestTimes; i++) {
				requestContent = requestNonsenseApi2();
				String sensitiveWord = SensitiveWordHelper.findFirst(nonsenseContent);
				if (StrUtil.isNotBlank(sensitiveWord)) {
					log.error("随机内容包含敏感词：{}", sensitiveWord);
				} else {
					nonsenseContent = requestContent;
					break;
				}
			}
			
			nonsense = new Nonsense();
			nonsense.setContent(nonsenseContent);
			return nonsense;
		} catch (Exception e) {
			log.error("随机内容请求失败，从数据库抓取!", e);
			return getNonsenseByDatabase();
		} finally {
			if (ObjectUtil.isNotNull(nonsense)) {
				nonsense.setSendTimes(ObjectUtil.isNotNull(nonsense.getSendTimes()) ? nonsense.getSendTimes() + 1 : 1);
				if (ObjectUtil.isNull(nonsense.getId())) {
					nonsense.setCreateTime(new Date());
					nonsense.setUpdateTime(nonsense.getCreateTime());
					nonsenseService.save(nonsense);
				} else {
					nonsense.setUpdateTime(new Date());
					nonsenseService.updateById(nonsense);
				}
			} else {
				log.error("随机内容获取失败，请检查数据库");
			}
		}
	}
	
	/**
	 * 请求随机内容
	 *
	 * @return
	 */
	public String requestNonsenseApi2() {
		String content = HttpUtil.get("https://api.uomg.com/api/rand.qinghua?format=text");
		if (StrUtil.isBlank(content)) {
			throw new RuntimeException("随机内容获取失败");
		}
		return content;
	}
	
	/**
	 * 请求随机内容
	 *
	 * @return
	 */
	@Deprecated(since = "已弃用，域名于25年6月到期了")
	public String requestNonsenseApi() {
		String content = HttpUtil.get("https://api.lovelive.tools/api/SweetNothings");
		if (StrUtil.isBlank(content)) {
			throw new RuntimeException("随机内容获取失败");
		}
		return content;
	}
	
	
	/**
	 * 从数据库获取随机内容
	 *
	 * @return
	 */
	private Nonsense getNonsenseByDatabase() {
		LambdaQueryWrapper<Nonsense> wrapper = Wrappers.lambdaQuery(Nonsense.class)
				.isNotNull(Nonsense::getContent)
				.orderByAsc(Nonsense::getSendTimes)
				.orderByAsc(Nonsense::getUpdateTime)
				.last("limit 10");
		List<Nonsense> nonsenseList = nonsenseService.list(wrapper);
		return RandomUtil.randomEle(nonsenseList);
	}
}
