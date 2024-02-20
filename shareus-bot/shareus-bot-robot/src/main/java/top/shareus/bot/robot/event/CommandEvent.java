package top.shareus.bot.robot.event;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.bot.robot.job.RestGroupMetaPwdTask;
import top.shareus.bot.robot.util.MessageChainUtils;

import javax.annotation.Resource;

/**
 * 自定义命令事件
 */
@Slf4j
@Component
public class CommandEvent {
	
	@Resource
	private RestGroupMetaPwdTask restGroupMetaPwdTask;
	
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.ADMIN, GroupEnum.TEST})
	public void exec(GroupMessageEvent event) {
		PlainText command = MessageChainUtils.fetchPlainText(event.getMessage());
		if (command == null) {
			return;
		}
		
		String content = command.getContent();
		if ("/help".equals(content)) {
			MessageChainBuilder singleMessages = new MessageChainBuilder();
			singleMessages.add("支持的命令有：\n");
			singleMessages.add("/help  查看所有命令\n");
			singleMessages.add("/resetAlistPwd  重置云盘群文件的密码\n");
			event.getGroup().sendMessage(singleMessages.build());
		}
		
		if ("/resetAlistPwd".equals(content)) {
			log.info("重置云盘密码");
			restGroupMetaPwdTask.run();
		}
		
	}
	
}
