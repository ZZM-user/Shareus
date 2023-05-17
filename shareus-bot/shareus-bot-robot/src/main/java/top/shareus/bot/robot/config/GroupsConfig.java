package top.shareus.bot.robot.config;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import top.shareus.bot.common.eumn.bot.GroupEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 群组配置
 *
 * @author 17602
 * @date 2023/05/11
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "group")
public class GroupsConfig {
	
	// 测试群组
	private List<Long> test;
	
	// 管理群组
	private List<Long> admin;
	
	// 聊天群组
	private List<Long> chat;
	
	// 资源群组
	private List<Long> res;
	
	// gpt群组
	private List<Long> gpt;
	
	/**
	 * 获取分组类型
	 *
	 * @return {@link HashMap}<{@link GroupEnum}, {@link List}<{@link Long}>>
	 */
	public HashMap<GroupEnum, List<Long>> getByGroup() {
		HashMap<GroupEnum, List<Long>> map = new HashMap<>();
		map.put(GroupEnum.ALL, getAll());
		map.put(GroupEnum.TEST, test);
		map.put(GroupEnum.ADMIN, admin);
		map.put(GroupEnum.CHAT, chat);
		map.put(GroupEnum.RES, res);
		map.put(GroupEnum.GPT, gpt);
		
		return map;
	}
	
	public List<Long> getAll() {
		Set<Long> unionDistinct = CollUtil.unionDistinct(test, admin, chat, res, gpt);
		return new ArrayList<>(unionDistinct);
	}
	
}
