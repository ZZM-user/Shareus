package top.shareus.bot.robot.job;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.domain.QMember;
import top.shareus.bot.robot.service.ArchivedFileService;
import top.shareus.bot.robot.service.QMemberService;
import top.shareus.bot.robot.util.QQInfoUtil;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class RefreshQQInfoJob {
	
	@Resource
	QQInfoUtil qqInfoUtil;
	
	@Resource
	private QMemberService qMemberService;
	
	@Resource
	private ArchivedFileService archivedFileService;
	
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void refresh() {
		log.info("开始拉取QQ信息");
		execute();
	}
	
	public void execute() {
		// 更新已有用户信息
		List<QMember> qMemberList = qMemberService.list();
		qMemberList.forEach(qMember -> {
			try {
				QQInfoUtil.QQInfo qqInfo = qqInfoUtil.getInfo(Convert.toStr(qMember.getQq()));
				if (ObjectUtil.isNotNull(qqInfo)) {
					qMember.setNickName(qqInfo.getName());
					qMember.setAvatarUrl(qqInfo.getAvatar());
				}
			} catch (Exception e) {
				log.error("拉取QQ信息失败", e);
			}
		});
		if (! qMemberList.isEmpty()) {
			qMemberService.updateBatchById(qMemberList);
		}
		
		// 更新新增用户信息
		List<Long> qqList = qMemberList.stream().map(QMember::getId).toList();
		
		LambdaQueryWrapper<ArchivedFile> queryWrapper = Wrappers.<ArchivedFile>lambdaQuery()
				.select(ArchivedFile::getSenderId)
				.isNotNull(ArchivedFile::getSenderId)
				.notIn(! qMemberList.isEmpty(), ArchivedFile::getSenderId, qqList)
				.groupBy(ArchivedFile::getSenderId);
		List<ArchivedFile> newQMemberList = archivedFileService.list(queryWrapper);
		
		List<QMember> newMemberList = newQMemberList.stream().map(n -> {
					try {
						QQInfoUtil.QQInfo qqInfo = qqInfoUtil.getInfo(Convert.toStr(n.getSenderId()));
						if (ObjectUtil.isNotNull(qqInfo)) {
							QMember member = new QMember();
							member.setQq(n.getSenderId());
							member.setNickName(qqInfo.getName());
							member.setAvatarUrl(qqInfo.getAvatar());
							return member;
						}
					} catch (Exception e) {
						log.error("拉取QQ信息失败", e);
					}
					return null;
				}).filter(ObjectUtil::isNotNull)
				.toList();
		
		if (! newMemberList.isEmpty()) {
			qMemberService.saveBatch(newMemberList);
		}
		
	}
}
