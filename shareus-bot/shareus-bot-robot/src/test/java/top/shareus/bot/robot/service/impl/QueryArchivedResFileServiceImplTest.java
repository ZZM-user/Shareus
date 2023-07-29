//package top.shareus.bot.robot.service.impl;
//
//import cn.hutool.core.util.ReUtil;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.regex.Pattern;
//
//class QueryArchivedResFileServiceImplTest {
//
//
//	@Test
//	void isQiuWen() {
//		boolean match = ReUtil.isMatch("^书名[：|:](.*?)\\n作者[：|:](.*?)\\n平台[：|:](.*?)$", "书名：\n" +
//				"作者：\n" +
//				"平台：");
//
//		System.out.println("match = " + match);
//
//		boolean match2 = ReUtil.isMatch("^书名[：|:](.*?)\\n作者[：|:](.*?)\\n平台[：|:](.*?)$", "书名:漂亮美人被强制的日常【快穿】\n" +
//				"作者:挽碧\n" +
//				"平台:海棠");
//		System.out.println("match2 = " + match2);
//
//		String content = "书名：都有\n" +
//				"作者：都有\n" +
//				"平台：";
//		// 获取求文信息
//		List<String> infoGroupList = ReUtil.getAllGroups(Pattern.compile("^书名[：|:](.*?)\\n作者[：|:](.*?)\\n平台[：|:](.*?)$"), content, false);
//
//		infoGroupList.forEach(System.out::println);
//		// 信息不全
//		boolean hasEmptyInfo = infoGroupList.stream().anyMatch(String::isEmpty);
//		if (hasEmptyInfo) {
//			System.out.println("\"信息不全\" = " + "信息不全");
//		}
//
//	}
//}