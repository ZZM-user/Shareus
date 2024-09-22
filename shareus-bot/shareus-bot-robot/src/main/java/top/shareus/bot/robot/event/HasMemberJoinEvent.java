package top.shareus.bot.robot.event;

import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.bot.robot.util.ImageUtils;

/**
 * 新成员加入事件
 *
 * @author 17602
 * @date 2022/8/28 15:51
 */
@Slf4j
@Component
public class HasMemberJoinEvent extends SimpleListenerHost {
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.ADMIN, GroupEnum.CHAT})
	public void onHasMemberJoinEvent(MemberJoinEvent event) {
		NormalMember member = event.getMember();
		MessageChainBuilder builder = new MessageChainBuilder();
		builder.add(new At(member.getId()));
		builder.add(" 欢迎欢迎！");
		// 构建头像 发送
		Image image = ImageUtils.create(event.getGroup(), member.getAvatarUrl());
		builder.add(image);
		event.getGroup().sendMessage(builder.build());
	}
	
	@Override
	public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
		log.error("{}\n{}\n{}", context, exception.getMessage(), exception.getCause().getMessage());
	}
}
