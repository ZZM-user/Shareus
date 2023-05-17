package top.shareus.bot.robot.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import top.shareus.bot.common.pojo.vo.NormalMemberVO;
import top.shareus.bot.robot.config.GroupsConfig;

import java.util.*;

/**
 * 群组 常用的工具类
 *
 * @author 17602
 * @date 2022/10/29 17:17
 */
@Slf4j
public class GroupUtils {
	
	private static GroupsConfig groupsConfig = SpringUtil.getBean(GroupsConfig.class);
	
	/**
	 * 得到当前机器人下 所有小组成员（全部：为某业务场景定制）
	 *
	 * @return {@link Map}<{@link String}, {@link ContactList}<{@link NormalMember}>>
	 */
	public static Map<String, List<NormalMemberVO>> getAllGroupMembers(Bot bot) {
		Map<String, List<NormalMemberVO>> contactListMap = new HashMap<>();
		
		// 读取 管理组成员
		List<NormalMemberVO> adminMemberList = GroupUtils.getGroupMembers(bot, groupsConfig.getAdmin());
		contactListMap.put("admin", adminMemberList);
		
		// 读取 资源组成员
		List<NormalMemberVO> resMemberList = GroupUtils.getGroupMembers(bot, groupsConfig.getRes());
		contactListMap.put("res", resMemberList);
		
		// 读取 聊天组成员
		List<NormalMemberVO> chatMemberList = GroupUtils.getGroupMembers(bot, groupsConfig.getChat());
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
	public static List<NormalMemberVO> getGroupMembers(Bot bot, List<Long> group) {
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
	public static Boolean invalidGroup
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
	
	/**
	 * 属于管理组
	 *
	 * @param groupId 组id
	 *
	 * @return boolean
	 */
	public static boolean isAdmin(Long groupId) {
		return hasGroups(groupsConfig.getAdmin(), groupId);
	}
	
	/**
	 * 属于某个组
	 *
	 * @param groups  组
	 * @param groupId 组id
	 *
	 * @return boolean
	 */
	public static boolean hasGroups(List<Long> groups, Long groupId) {
		Long hasGroup = groups.stream().filter(r -> r.equals(groupId)).findAny().orElse(null);
		return ObjectUtil.isNotNull(hasGroup);
	}
	
	/**
	 * 属于资源组
	 *
	 * @param groupId 组id
	 *
	 * @return boolean
	 */
	public static boolean isRes(Long groupId) {
		return hasGroups(groupsConfig.getRes(), groupId);
	}
	
	/**
	 * 属于聊天组
	 *
	 * @param groupId 组id
	 *
	 * @return boolean
	 */
	public static boolean isChat(Long groupId) {
		return hasGroups(groupsConfig.getChat(), groupId);
	}
	
	/**
	 * 属于测试组
	 *
	 * @param groupId 组id
	 *
	 * @return boolean
	 */
	public static boolean isTest(Long groupId) {
		return hasGroups(groupsConfig.getTest(), groupId);
	}
	
	/**
	 * 不属于某个组
	 *
	 * @param groups  组
	 * @param groupId 组id
	 *
	 * @return boolean
	 */
	public static boolean notHasGroups(List<Long> groups, Long groupId) {
		return ! hasGroups(groups, groupId);
	}
	
	/**
	 * 不属于某些组
	 *
	 * @param groups  组
	 * @param groupId 组id
	 *
	 * @return boolean
	 */
	@SafeVarargs
	public static boolean notHasAnyGroups(Long groupId, List<Long>... groups) {
		return ! hasAnyGroups(groupId, groups);
	}
	
	/**
	 * 属于某些组
	 *
	 * @param groups  组
	 * @param groupId 组id
	 *
	 * @return boolean
	 */
	@SafeVarargs
	public static boolean hasAnyGroups(Long groupId, List<Long>... groups) {
		// 全部组合到一起
		List<Long> allGroups = new ArrayList<>(groups.length);
		Arrays.stream(groups).forEach(allGroups::addAll);
		
		if (CollUtil.isEmpty(allGroups)) {
			return false;
		}
		
		return hasGroups(allGroups, groupId);
	}
}
