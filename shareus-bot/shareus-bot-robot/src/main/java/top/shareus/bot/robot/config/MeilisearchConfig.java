package top.shareus.bot.robot.config;

import cn.hutool.core.util.ObjectUtil;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MeilisearchConfig {
	
	@Value("${meilisearch.url:http://127.0.01:7700}")
	private String meilisearchUrl;
	@Value("${meilisearch.masterKey:}")
	private String masterKey = "masterKey";
	
	private volatile Client client;
	
	/**
	 * 获取 MeiliSearch 客户端
	 *
	 * @return
	 */
	public Client getClient() {
		if (ObjectUtil.isNull(client)) {
			synchronized (this) {
				if (ObjectUtil.isNull(client)) {
					client = buildClient();
				}
			}
		}
		return client;
	}
	
	/**
	 * 构建 MeiliSearch 客户端
	 *
	 * @return
	 */
	public Client buildClient() {
		Config config = new Config(meilisearchUrl, masterKey, new JacksonJsonHandler());
		client = new Client(config);
		log.info("MeiliSearch Client 初始化完成");
		return client;
	}
	
}
