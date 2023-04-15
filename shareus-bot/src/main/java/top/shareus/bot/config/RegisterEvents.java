package top.shareus.bot.config;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import top.shareus.bot.event.*;
import top.shareus.common.core.utils.SpringUtils;

/**
 * 注册事件
 *
 * @author zhaojl
 * @date 2023/01/26
 */
@Slf4j
public class RegisterEvents {

    /**
     * 注册群组消息事件
     */
    public static void registerGroupMessageEvent() {
        log.info("开始注册-群组消息事件");
        EventChannel<GroupMessageEvent> channel = GlobalEventChannel.INSTANCE.filterIsInstance(GroupMessageEvent.class);
        channel.registerListenerHost(SpringUtils.getBean(ResChatEvent.class));
        channel.registerListenerHost(SpringUtils.getBean(ForwardAdminMessage.class));
        channel.registerListenerHost(SpringUtils.getBean(ArchivedResFile.class));
        channel.registerListenerHost(SpringUtils.getBean(QueryArchivedResFile.class));
    }

    /**
     * 注册群员加入事件
     */
    public static void registerMemberJoinEvent() {
        log.info("开始注册-成员加入事件");
        EventChannel<MemberJoinEvent> channel = GlobalEventChannel.INSTANCE.filterIsInstance(MemberJoinEvent.class);
        channel.registerListenerHost(SpringUtils.getBean(HasMemberJoinEvent.class));
        channel.registerListenerHost(SpringUtils.getBean(MemberJoinRequest.class));
    }

    /**
     * 注册群消息撤回事件
     */
    public static void registerGroupRecall() {
        log.info("开始注册-群消息撤回事件");
        EventChannel<MessageRecallEvent.GroupRecall> channel = GlobalEventChannel.INSTANCE.filterIsInstance(MessageRecallEvent.GroupRecall.class);
        channel.registerListenerHost(SpringUtils.getBean(RecallQueryArchivedEvent.class));
    }

    /**
     * 注册好友消息事件
     */
    public static void registerFriendMessageEvent() {
        log.info("开始注册-好友消息事件");
        EventChannel<FriendMessageEvent> channel = GlobalEventChannel.INSTANCE.filterIsInstance(FriendMessageEvent.class);
        channel.registerListenerHost(SpringUtils.getBean(ForwardFriendFlashImageMessage.class));
    }

    /**
     * 注册机器人被邀请进群消息事件
     */
    public static void registerJoinGroupEvent() {
        log.info("开始注册-好友消息事件");
        EventChannel<FriendMessageEvent> channel = GlobalEventChannel.INSTANCE.filterIsInstance(FriendMessageEvent.class);
        channel.registerListenerHost(SpringUtils.getBean(JoinGroupEvent.class));
    }
}
