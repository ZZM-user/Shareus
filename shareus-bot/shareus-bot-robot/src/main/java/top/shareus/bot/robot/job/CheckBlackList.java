package top.shareus.bot.robot.job;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.domain.BlackList;
import top.shareus.bot.common.pojo.vo.NormalMemberVO;
import top.shareus.bot.robot.config.BotManager;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.mapper.BlackListMapper;
import top.shareus.bot.robot.service.GroupService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CheckBlackList {
	@Autowired
	private BlackListMapper blacklistMapper;
	@Autowired
	private GroupsConfig groupConfig;
	@Autowired
	private GroupService groupService;
	
	@Scheduled(cron = "0 0 19 * * ?")
	public void check() {
		MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
		messageChainBuilder.add("黑名单域：\n");
		Bot bot = BotManager.getBot();
		List<BlackList> blackLists = blacklistMapper.selectList(new QueryWrapper<>());
		
		Map<String, List<NormalMemberVO>> allGroupMembers = groupService.getAllGroupMembers(bot);
		List<NormalMemberVO> chatMemberList = allGroupMembers.get("chat");
		List<NormalMemberVO> resMemberList = allGroupMembers.get("res");
		
		blackLists.parallelStream().forEach(black -> {
			List<NormalMemberVO> chatList = chatMemberList.parallelStream().filter(member -> member.getId().equals(black.getId())).toList();
			List<NormalMemberVO> resList = resMemberList.parallelStream().filter(member -> member.getId().equals(black.getId())).toList();
			String chatGroups = chatList.stream().map(NormalMemberVO::getGroupName).collect(Collectors.joining(","));
			String resGroups = resList.stream().map(NormalMemberVO::getGroupName).collect(Collectors.joining(","));
			
			MessageChainBuilder msgBuilder = new MessageChainBuilder();
			msgBuilder.add(new At(black.getId()));
			msgBuilder.add("\n依然存在于：\n");
			if (StrUtil.isEmpty(chatGroups) && StrUtil.isEmpty(resGroups)) {
				msgBuilder.clear();
			} else if (StrUtil.isNotEmpty(chatGroups)) {
				msgBuilder.add("聊天主群：" + chatGroups);
			} else if (StrUtil.isNotEmpty(resGroups)) {
				msgBuilder.add("资源群：" + resGroups);
			}
			
			if (! msgBuilder.isEmpty()) {
				msgBuilder.add("--------------------------------");
				messageChainBuilder.add(msgBuilder.build());
			}
		});
		
		if (! messageChainBuilder.isEmpty()) {
			bot.getGroup(groupConfig.getAdmin().get(0)).sendMessage(messageChainBuilder.build());
		}
		
		log.info("黑名单检测成功……");
	}
	
}
