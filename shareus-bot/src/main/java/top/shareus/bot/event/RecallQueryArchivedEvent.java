package top.shareus.bot.event;

import cn.hutool.core.date.DateUtil;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.job.querylog.Polling;
import top.shareus.bot.mapper.QueryLogMapper;
import top.shareus.bot.util.GroupUtils;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.domain.entity.QueryLog;

import java.util.List;

/**
 * 关闭已撤回的求文
 *
 * @author 17602
 * @date 2023/1/26 15:51
 */
@Slf4j
@Component
public class RecallQueryArchivedEvent extends SimpleListenerHost {
	@Autowired
	private QueryLogMapper queryLogMapper;
	
	@EventHandler
	public void onRecallQueryArchivedEvent(MessageRecallEvent.GroupRecall event) {
		Group group = event.getGroup();
		boolean hasGroups = GroupUtils.hasGroups(GroupsConstant.RES_GROUPS, group.getId());
		if (! hasGroups) {
			return;
		}
		
		Member operator = event.getOperator();
		NormalMember author = event.getAuthor();
		int messageTime = event.getMessageTime() * 1000;
		List<QueryLog> queryLogs = queryLogMapper.selectUnfinishedQueryBySender(author.getId(), DateUtil.date(messageTime));
		
		String cause = "由 " + operator.getNameCard() + " 主动撤回";
		queryLogs.forEach(q -> Polling.stopQuery(q, cause));
		log.info(cause + " - " + queryLogs.size());
	}
	
	@Override
	public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
		log.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
	}
}
