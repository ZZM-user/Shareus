package top.shareus.bot.robot.command;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import top.shareus.bot.common.pojo.vo.NormalMemberVO;
import top.shareus.bot.robot.config.BotManager;
import top.shareus.bot.robot.config.GroupsConfig;
import top.shareus.bot.robot.util.ExcelUtils;
import top.shareus.bot.robot.util.GroupUploadFileUtils;
import top.shareus.bot.robot.util.GroupUtils;
import top.shareus.bot.robot.util.NormalMemberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 清除指定群成员
 *
 * @author 17602
 * @date 2022/8/28 13:36
 */
@Slf4j
public class ClearGroupMemberCommand extends JRawCommand {
	public final static ClearGroupMemberCommand INSTANCE = new ClearGroupMemberCommand();
	
	@Autowired
	private GroupsConfig groupsConfig;
	
	public ClearGroupMemberCommand() {
		// 使用插件主类对象作为指令拥有者；设置主指令名为 "test"
		super(INSTANCE.getOwner(), "clearMember");
		// 可选设置如下属性
		// 设置用法，这将会在 /help 中展示
		setUsage("/clearMember <group>");
		// 设置描述，也会在 /help 中展示
		setDescription("清除指定群组的僵尸群员");
		// 设置指令前缀是可选的，即使用 `test` 也能执行指令而不需要 `/test`
		setPrefixOptional(true);
	}
	
	@Override
	public void onCommand(@NotNull CommandContext context, @NotNull MessageChain args) {
		CommandSender sender = context.getSender();
		
		if (args.size() == 0) {
			sender.sendMessage("参数数量不正确 eg: " + getUsage() + " 123345");
			return;
		}
		
		Bot bot = BotManager.getBot();
		Group group = bot.getGroup(Long.parseLong(args.get(0).toString()));
		Group adminGroup = bot.getGroup(groupsConfig.getAdmin().get(0));
		
		if (ObjectUtil.isNull(group) || GroupUtils.isAdmin(group.getId())) {
			String message = "该群组成员获取失败!\n group：" + args.get(0);
			log.error(message);
			sender.sendMessage(message);
			return;
		}
		
		ContactList<NormalMember> members = group.getMembers();
		ContactList<NormalMember> adminGroupMembers = adminGroup.getMembers();
		List<NormalMemberVO> invalidMembers = new ArrayList<>();
		
		for (NormalMember member : members) {
			long id = member.getId();
			
			// 管理员不管
			NormalMember adminMember = adminGroupMembers.stream().filter(a -> a.getId() == id).findAny().orElse(null);
			if (ObjectUtil.isNotNull(adminMember)) {
				continue;
			}
			
			long timeStamp = member.getLastSpeakTimestamp() * 1000L;
			long diffMonth = DateUtil.between(DateUtil.date(timeStamp), DateUtil.date(), DateUnit.DAY);
			
			// 三月以上未发言者
			if (diffMonth >= 3 * 30) {
				invalidMembers.add(NormalMemberUtils.toMemberVO(member, "三月以上未发言者"));
				String message = "长时间未发言被移出";
//                member.kick(message);
				log.info(member.getId() + "-" + member.getNick() + "-" + message);
				continue;
			}
			
			// 两月以上未发言者 且未备注请假
			if ((diffMonth >= 2 * 30) && ! (StrUtil.containsAny(member.getNameCard(), "请假"))) {
				invalidMembers.add(NormalMemberUtils.toMemberVO(member, "两月以上未发言者 且未备注请假"));
				String message = "长时间未发言被移出";
//                member.kick(message);
				log.info(member.getId() + "-" + member.getNick() + "-" + message);
			}
			
		}
		String path = ExcelUtils.exportMemberDataExcel(invalidMembers, group.getName() + " - 失效群员名单");
		GroupUploadFileUtils.uploadFile(bot.getGroup(groupsConfig.getAdmin().get(0)), path);
	}
}
