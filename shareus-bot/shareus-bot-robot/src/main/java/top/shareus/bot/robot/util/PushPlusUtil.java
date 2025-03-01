package top.shareus.bot.robot.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class PushPlusUtil {
	
	private static final Logger log = LoggerFactory.getLogger(PushPlusUtil.class);
	
	
	/**
	 * 推送消息
	 *
	 * @param title
	 * @param content
	 *
	 * @return
	 */
	public static String pushMsg(String title, String content) {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("token", "a50c2fb8b10143448c52cd3c39ef3933");
		hashMap.put("title", title);
		hashMap.put("content", content);
		
		HttpResponse httpResponse = HttpRequest.post("https://www.pushplus.plus/send/")
				.body(JSONUtil.toJsonStr(hashMap))
				.execute();
		
		log.info("推送结果==>{}", httpResponse.body());
		return httpResponse.body();
	}
}
