package top.shareus.bot.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;

/**
 * 禁言 工具类
 *
 * @author zhaojl
 * @date 2023/01/14
 */
@Slf4j
public class MuteUtils {

    public static boolean mute(Group group, Long id) {
        return mute(group, id, 600L);
    }

    public static boolean mute(Group group, Long id, Long seconds) {
        NormalMember member = group.getMembers().get(id);
        if (ObjectUtil.isNull(member)) {
            log.info("在 " + group.getName() + " 中找不到该用户：" + id + "\t禁言失败！");
            return false;
        }

        member.mute(Math.toIntExact(seconds));
        log.info("禁言" + id + (seconds + "s"));

        return member.isMuted();
    }

    public static boolean mute(NormalMember member) {
        return mute(member, 600L);
    }

    public static boolean mute(NormalMember member, Long seconds) {
        if (ObjectUtil.isNull(member)) {
            log.info("找不到该用户：" + member + "\t禁言失败！");
            return false;
        }

        member.mute(Math.toIntExact(seconds));
        log.info("禁言" + member.getId() + (seconds + "s"));

        return member.isMuted();
    }
}
