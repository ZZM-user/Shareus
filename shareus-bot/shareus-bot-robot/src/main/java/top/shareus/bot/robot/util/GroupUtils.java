//package top.shareus.bot.robot.util;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.ObjectUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import top.shareus.bot.robot.config.GroupsConfig;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * 群组 常用的工具类
// *
// * @author 17602
// * @date 2022/10/29 17:17
// */
//@Slf4j
//@Component
//public class GroupUtils {
//
//	private static GroupsConfig groupsConfig;
//
//	@Autowired
//	public void setRedisTemplate(GroupsConfig groupsConfig) {
//		GroupUtils.groupsConfig = groupsConfig;
//	}
//
//
//	/**
//	 * 属于管理组
//	 *
//	 * @param groupId 组id
//	 *
//	 * @return boolean
//	 */
//	public static boolean isAdmin(Long groupId) {
//		return hasGroups(groupsConfig.getAdmin(), groupId);
//	}
//
//	/**
//	 * 属于某个组
//	 *
//	 * @param groups  组
//	 * @param groupId 组id
//	 *
//	 * @return boolean
//	 */
//	public static boolean hasGroups(List<Long> groups, Long groupId) {
//		Long hasGroup = groups.stream().filter(r -> r.equals(groupId)).findAny().orElse(null);
//		return ObjectUtil.isNotNull(hasGroup);
//	}
//
//	/**
//	 * 属于资源组
//	 *
//	 * @param groupId 组id
//	 *
//	 * @return boolean
//	 */
//	public static boolean isRes(Long groupId) {
//		return hasGroups(groupsConfig.getRes(), groupId);
//	}
//
//	/**
//	 * 属于聊天组
//	 *
//	 * @param groupId 组id
//	 *
//	 * @return boolean
//	 */
//	public static boolean isChat(Long groupId) {
//		return hasGroups(groupsConfig.getChat(), groupId);
//	}
//
//	/**
//	 * 属于测试组
//	 *
//	 * @param groupId 组id
//	 *
//	 * @return boolean
//	 */
//	public static boolean isTest(Long groupId) {
//		return hasGroups(groupsConfig.getTest(), groupId);
//	}
//
//	/**
//	 * 不属于某个组
//	 *
//	 * @param groups  组
//	 * @param groupId 组id
//	 *
//	 * @return boolean
//	 */
//	public static boolean notHasGroups(List<Long> groups, Long groupId) {
//		return ! hasGroups(groups, groupId);
//	}
//
//	/**
//	 * 不属于某些组
//	 *
//	 * @param groups  组
//	 * @param groupId 组id
//	 *
//	 * @return boolean
//	 */
//	@SafeVarargs
//	public static boolean notHasAnyGroups(Long groupId, List<Long>... groups) {
//		return ! hasAnyGroups(groupId, groups);
//	}
//
//	/**
//	 * 属于某些组
//	 *
//	 * @param groups  组
//	 * @param groupId 组id
//	 *
//	 * @return boolean
//	 */
//	@SafeVarargs
//	public static boolean hasAnyGroups(Long groupId, List<Long>... groups) {
//		// 全部组合到一起
//		List<Long> allGroups = new ArrayList<>(groups.length);
//		Arrays.stream(groups).forEach(allGroups::addAll);
//
//		if (CollUtil.isEmpty(allGroups)) {
//			return false;
//		}
//
//		return hasGroups(allGroups, groupId);
//	}
//}
