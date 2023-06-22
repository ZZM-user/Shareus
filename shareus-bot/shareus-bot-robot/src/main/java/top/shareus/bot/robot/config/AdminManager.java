package top.shareus.bot.robot.config;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.pojo.vo.NormalMemberVO;
import top.shareus.bot.common.redis.service.RedisService;
import top.shareus.bot.robot.service.GroupService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员操作中心
 *
 * @author 17602
 * @date 2023/06/22
 */
@Slf4j
@Component
public class AdminManager {
	
	public static final String REDIS_KEY = "admin-info";
	public static final Long REDIS_EXPIRE = 24L * 3600;
	
	@Autowired
	private RedissonClient redissonClient;
	@Autowired
	private RedisService redisService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private GroupsConfig groupConfig;
	@Autowired
	private BotManager botManager;
	
	public List<Long> getAllAdmin() {
		RLock lock = redissonClient.getLock("get-admin-info");
		
		lock.lock();
		try {
			List<Object> idList = redisService.getCacheList(REDIS_KEY);
			if (CollUtil.isEmpty(idList)) {
				List<NormalMemberVO> groupMembers = groupService.getGroupMembers(BotManager.getBot(), groupConfig.getAdmin());
				idList = groupMembers.parallelStream().map(NormalMemberVO::getId).collect(Collectors.toList());
				redisService.setCacheList(REDIS_KEY, idList);
				redisService.expire(REDIS_KEY, REDIS_EXPIRE);
			}
			
			return idList.parallelStream().map(id -> Long.valueOf((String) id)).toList();
		} finally {
			lock.unlock();
		}
	}
}
