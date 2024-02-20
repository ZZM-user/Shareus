package top.shareus.bot.robot.config;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import top.shareus.bot.robot.event.*;

/**
 * 注册事件
 *
 * @author zhaojl
 * @date 2023/01/26
 */
@Slf4j
@Configuration
public class RegisterEvents {
	
	@Autowired
	private ResChatEvent resChatEvent;
	@Autowired
	private ForwardAdminMessage forwardAdminMessage;
	@Autowired
	private ArchivedResFile archiveFile;
	@Autowired
	private QueryArchivedResFile queryArchivedResFile;
	@Autowired
	private HasMemberJoinEvent hasMemberJoinEvent;
	@Autowired
	private MemberJoinRequest memberJoinRequest;
	@Autowired
	private RecallQueryArchivedEvent recallQueryArchivedEvent;
	@Autowired
	private ForwardFriendFlashImageMessage forwardedFriendFlashImageMessage;
	@Autowired
	private JoinGroupEvent joinGroupEvent;
	@Autowired
	private AtMeTalkEvent atMeTalkEvent;
	@Autowired
	private BlackListManager blacklistManager;
	@Autowired
	private TalkativeHonorChangeEvent talkativeHonorChangeEvent;
	@Autowired
	private BotIsOfflineEvent botIsOfflineEvent;
	@Autowired
	private CommandEvent commandEvent;
	
	/**
	 * 注册群组消息事件
	 */
	public void registerGroupMessageEvent() {
		log.info("开始注册-群组消息事件");
		EventChannel<BotEvent> channel = getBotChannel();
		channel.subscribeAlways(GroupMessageEvent.class, resChatEvent::onAdminGroupMessageEvent);
		channel.subscribeAlways(GroupMessageEvent.class, archiveFile::onArchivedResFileEvent);
		channel.subscribeAlways(GroupMessageEvent.class, forwardAdminMessage::onResGroupMessageEvent);
		channel.subscribeAlways(GroupMessageEvent.class, queryArchivedResFile::onQueryArchivedResFile);
		channel.subscribeAlways(GroupMessageEvent.class, atMeTalkEvent::onAtMeTalkEvent);
		channel.subscribeAlways(GroupMessageEvent.class, blacklistManager::onBlackListManager);
		channel.subscribeAlways(GroupMessageEvent.class, commandEvent::exec);
	}
	
	/**
	 * 获取机器人通道
	 *
	 * @return {@link EventChannel}<{@link BotEvent}>
	 */
	private EventChannel<BotEvent> getBotChannel() {
		log.info("开始获取机器人消息通道");
		Bot bot = BotManager.getBot();
		return bot.getEventChannel();
	}
	
	/**
	 * 注册群员加入事件
	 */
	public void registerMemberJoinEvent() {
		log.info("开始注册-成员加入事件");
		EventChannel<BotEvent> channel = getBotChannel();
		channel.subscribeAlways(MemberJoinEvent.class, hasMemberJoinEvent::onHasMemberJoinEvent);
	}
	
	/**
	 * 注册成员申请加入事件
	 */
	public void registerMemberJoinRequestEvent() {
		log.info("开始注册-成员申请加入事件");
		EventChannel<BotEvent> channel = getBotChannel();
		channel.subscribeAlways(MemberJoinRequestEvent.class, memberJoinRequest::onMemberJoinRequest);
		
	}
	
	/**
	 * 注册群消息撤回事件
	 */
	public void registerGroupRecall() {
		log.info("开始注册-群消息撤回事件");
		EventChannel<BotEvent> channel = getBotChannel();
		channel.subscribeAlways(MessageRecallEvent.GroupRecall.class, recallQueryArchivedEvent::onRecallQueryArchivedEvent);
	}
	
	/**
	 * 注册好友消息事件
	 */
	public void registerFriendMessageEvent() {
		log.info("开始注册-好友消息事件");
		EventChannel<BotEvent> channel = getBotChannel();
		channel.subscribeAlways(FriendMessageEvent.class, forwardedFriendFlashImageMessage::onFriendFlashImageMessageEvent);
	}
	
	/**
	 * 注册机器人被邀请进群消息事件
	 */
	public void registerBotJoinGroupEvent() {
		log.info("开始注册-加入新群事件");
		EventChannel<BotEvent> channel = getBotChannel();
		channel.subscribeAlways(BotJoinGroupEvent.class, joinGroupEvent::onJoinGroupEvent);
	}
	
	/**
	 * 注册群成员荣誉变更事件
	 */
	public void registerMemberHonorChangeEvent() {
		log.info("开始注册-群成员荣誉变更事件");
		EventChannel<BotEvent> channel = getBotChannel();
		channel.subscribeAlways(MemberHonorChangeEvent.class, talkativeHonorChangeEvent::onTalkativeHonorChangeEvent);
	}
	
	/**
	 * 注册机器人离线事件
	 */
	public void registerBotOfflineEvent() {
		log.info("开始注册-机器人离线事件");
		EventChannel<BotEvent> channel = getBotChannel();
		channel.subscribeAlways(BotOfflineEvent.class, botIsOfflineEvent::onBotIsOfflineEvent);
	}
}
