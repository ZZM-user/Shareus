package top.shareus.bot.robot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.shareus.bot.common.domain.QMember;

/**
 * QQ成员(QMember)表服务接口
 *
 * @author zzm
 * @since 2024-10-10 20:39:47
 */
public interface QMemberService extends IService<QMember> {
	
	QMember selectByQQ(String qq);
}

