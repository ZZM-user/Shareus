package top.shareus.bot.robot.service.impl;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.pojo.vo.NormalMemberVO;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.service.GroupService;
import top.shareus.bot.robot.util.NormalMemberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群组服务impl
 *
 * @author 17602
 * @date 2023/05/21
 */
@Slf4j
@Service
public class GroupServiceImpl implements GroupService {
	
	
	@Autowired
	private GroupsConfig groupsConfig;
	
	
	/**
	 * 得到当前机器人下 所有小组成员（全部：为某业务场景定制）
	 *
	 * @return {@link Map}<{@link String}, {@link ContactList}<{@link NormalMember}>>
	 */
	@Override
	public Map<String, List<NormalMemberVO>> getAllGroupMembers(Bot bot) {
		Map<String, List<NormalMemberVO>> contactListMap = new HashMap<>();
		
		// 读取 管理组成员
		List<NormalMemberVO> adminMemberList = this.getGroupMembers(bot, groupsConfig.getAdmin());
		contactListMap.put("admin", adminMemberList);
		
		// 读取 资源组成员
		List<NormalMemberVO> resMemberList = this.getGroupMembers(bot, groupsConfig.getRes());
		contactListMap.put("res", resMemberList);
		
		// 读取 聊天组成员
		List<NormalMemberVO> chatMemberList = this.getGroupMembers(bot, groupsConfig.getChat());
		contactListMap.put("chat", chatMemberList);
		
		return contactListMap;
	}
	
	/**
	 * 获取当前机器人下的指定群组成员列表
	 *
	 * @param bot
	 * @param group
	 *
	 * @return
	 */
	@Override
	public List<NormalMemberVO> getGroupMembers(Bot bot, List<Long> group) {
		List<NormalMemberVO> groupList = new ArrayList<>();
		Group groupTemp;
		for (Long number : group) {
			groupTemp = bot.getGroup(number);
			if (ObjectUtil.isNotNull(groupTemp)) {
				ContactList<NormalMember> members = groupTemp.getMembers();
				List<NormalMemberVO> collect = members.stream().map(NormalMemberUtils::toMemberVO).toList();
				groupList.addAll(collect);
				log.debug(groupTemp.getName() + "\n" + collect.size() + "成员");
			} else {
				log.error("获取群成员列表失败：" + number);
			}
		}
		return groupList;
	}
	
	/**
	 * 检查必备的群是否加载成功
	 *
	 * @param adminMemberList
	 * @param resMemberList
	 * @param chatMemberList
	 *
	 * @return
	 */
	@Override
	public Boolean invalidGroup
	(List<NormalMemberVO> adminMemberList, List<NormalMemberVO> resMemberList, List<NormalMemberVO> chatMemberList) {
		if (adminMemberList.isEmpty()) {
			log.error("管理群组加载失败！");
		}
		if (resMemberList.isEmpty()) {
			log.error("资源群组加载失败！");
			return false;
		}
		if (chatMemberList.isEmpty()) {
			log.error("聊天群组加载失败！");
			return false;
		}
		return true;
	}
	
}
