package top.shareus.bot.robot.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.domain.QMember;
import top.shareus.bot.robot.mapper.QMemberMapper;
import top.shareus.bot.robot.service.QMemberService;
import top.shareus.bot.robot.util.QQInfoUtil;

import javax.annotation.Resource;

/**
 * QQ成员(QMember)表服务实现类
 *
 * @author zzm
 * @since 2024-10-10 20:39:47
 */
@Slf4j
@Service
public class QMemberServiceImpl extends ServiceImpl<QMemberMapper, QMember> implements QMemberService {
	
	@Resource
	private QQInfoUtil qqInfoUtil;
	
	@Override
	public QMember selectByQQ(String qq) {
		if (StrUtil.isBlank(qq)) {
			return null;
		}
		
		QMember member = getOne(Wrappers.<QMember>lambdaQuery().eq(QMember::getQq, qq), false);
		if (ObjectUtil.isNotNull(member)) {
			return member;
		}
		
		QQInfoUtil.QQInfo info = qqInfoUtil.getInfo(qq);
		if (ObjectUtil.isNull(info)) {
			return null;
		}
		
		member.setQq(Convert.toLong(qq));
		member.setAvatarUrl(info.getAvatar());
		member.setNickName(info.getName());
		save(member);
		return member;
	}
}

