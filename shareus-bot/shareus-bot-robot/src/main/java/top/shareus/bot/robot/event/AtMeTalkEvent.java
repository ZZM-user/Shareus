package top.shareus.bot.robot.event;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.bot.robot.util.MessageChainUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * At我 事件
 *
 * @author zhaojl
 * @date 2023/03/01
 */
@Slf4j
@Component
public class AtMeTalkEvent extends SimpleListenerHost {
	
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.GPT})
	public void onAtMeTalkEvent(GroupMessageEvent event) {
		
		At at = MessageChainUtils.fetchAt(event.getMessage());
		if (ObjectUtil.isNull(at)) {
			log.debug("不是@信息");
			return;
		}
		long target = at.getTarget();
		if (event.getBot().getId() != target) {
			log.debug("不是@我");
			return;
		}
		log.info(event.getSenderName() + " 艾特我了");
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		PlainText plainText = MessageChainUtils.fetchPlainText(event.getMessage());
		map.put("sender", event.getSenderName());
		map.put("prompt", plainText.getContent());
		
		MessageChainBuilder chainBuilder = new MessageChainBuilder();
		chainBuilder.append(new At(event.getSender().getId()));
		chainBuilder.append(" ");

//        String talk = GPTUtil.talk(event.getSenderName(), plainText.getContent());
//        if (StrUtil.isNotBlank(talk)) {
//            chainBuilder.append(talk);
//            event.getGroup().sendMessage(chainBuilder.build());
//            return;
//        }
		
		HttpResponse execute = null;
		try {
			execute = HttpRequest.post("http://124.220.67.51:8459/ChatGPT/api")
					.body(JSONUtil.toJsonStr(map))
					.timeout(500000)
					.execute();
		} finally {
			log.info(execute.body());
			if (ObjectUtil.isNotNull(execute) && ObjectUtil.isNotNull(execute.body()) && execute.isOk()) {
				Map<String, String> msg = JSONUtil.toBean(execute.body(), Map.class);
				String answer = msg.get("answer");
				chainBuilder.append(StrUtil.isNotBlank(answer) ? answer : "容我喝杯咖啡休息一下，谢谢");
			} else {
				chainBuilder.append(" 我被玩坏了,先别找我了");
			}
			event.getGroup().sendMessage(chainBuilder.build());
		}
	}
	
	@Override
	public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
		log.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause());
	}
	
}
