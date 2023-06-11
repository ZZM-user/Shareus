package top.shareus.bot.robot.service;

import net.mamoe.mirai.Bot;
import top.shareus.bot.common.pojo.vo.NormalMemberVO;

import java.util.List;
import java.util.Map;

/**
 * 群组服务
 *
 * @author 17602
 * @date 2023/05/21
 */
public interface GroupService {
	Map<String, List<NormalMemberVO>> getAllGroupMembers(Bot bot);
	
	List<NormalMemberVO> getGroupMembers(Bot bot, List<Long> group);
	
	Boolean invalidGroup
			(List<NormalMemberVO> adminMemberList, List<NormalMemberVO> resMemberList, List<NormalMemberVO> chatMemberList);
}
