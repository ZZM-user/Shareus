package top.shareus.bot.robot.util;


import cn.hutool.core.date.StopWatch;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class SinkShortUrlUtil {
	
	private static final String URL_PERFFIX = "https://url.shareus.top";
	
	private static final String AUTHORIZATION = "ZJL20001003";
	
	public static void main(String[] args) {
		StopWatch stopWatch = StopWatch.create("1");
		stopWatch.start();
		for (int i = 0; i < 10; i++) {
			generateShortUrl("https://pan.shareus.top/r2/2025/03/07/%E5%82%AC%E7%9C%A0%E7%9B%B4%E6%92%AD%E9%A1%B6%E6%B5%81%E7%94%B7%E5%9B%A2%E7%9A%84yin%E5%A0%95%20%E4%BD%9C%E8%80%85%EF%BC%9A%E7%A6%8F%E4%B8%B4%E9%97%A8%EF%BC%88%E5%AF%86%E7%A0%81stla(1).zip");
		}
		stopWatch.stop();
		log.info("{}", stopWatch.prettyPrint());
		log.info("{}", stopWatch.getTotalTimeMillis());
	}
	
	/**
	 * 生成短url
	 *
	 * @param longUrl 长url
	 *
	 * @return {@code String}
	 */
	public static String generateShortUrl(String longUrl) {
		HashMap<String, String> map = new HashMap(3) {{
			put("url", longUrl);
		}};
		
		HttpResponse response = HttpRequest.post(URL_PERFFIX + "/api/link/create")
				.body(JSONUtil.toJsonPrettyStr(map), ContentType.JSON.getValue())
				.header("authorization", "Bearer " + AUTHORIZATION)
				.execute().sync();
		
		String body = response.body();
		log.info("获取短连接结果：{}", body);
		if (response.isOk()) {
			
			String shortUrl = JSONObject.parseObject(body).getString("shortLink");
			
			log.info("生产短连接成功：{}", shortUrl);
			return shortUrl;
		}
		
		log.error("生产短连接失败！{}", longUrl);
		
		throw new RuntimeException("生产短连接失败！");
	}
}
