package top.shareus.bot.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.domain.QMember;
import top.shareus.bot.web.mapper.QMemberMapper;
import top.shareus.bot.web.service.QMemberService;
import top.shareus.common.core.utils.DateUtils;

import java.util.List;

/**
 * QQ成员Service业务层处理
 *
 * @author zhaojl
 * @date 2023-04-02
 */
@Service
public class QMemberServiceImpl implements QMemberService {
	@Autowired
	private QMemberMapper qMemberMapper;
	
	/**
	 * 查询QQ成员
	 *
	 * @param id QQ成员主键
	 *
	 * @return QQ成员
	 */
	@Override
	public QMember selectQMemberById(Long id) {
		return qMemberMapper.selectQMemberById(id);
	}
	
	/**
	 * 查询QQ成员列表
	 *
	 * @param qMember QQ成员
	 *
	 * @return QQ成员
	 */
	@Override
	public List<QMember> selectQMemberList(QMember qMember) {
		return qMemberMapper.selectQMemberList(qMember);
	}
	
	/**
	 * 新增QQ成员
	 *
	 * @param qMember QQ成员
	 *
	 * @return 结果
	 */
	@Override
	public int insertQMember(QMember qMember) {
		qMember.setCreateTime(DateUtils.getNowDate());
		return qMemberMapper.insertQMember(qMember);
	}
	
	/**
	 * 修改QQ成员
	 *
	 * @param qMember QQ成员
	 *
	 * @return 结果
	 */
	@Override
	public int updateQMember(QMember qMember) {
		qMember.setUpdateTime(DateUtils.getNowDate());
		return qMemberMapper.updateQMember(qMember);
	}
	
	/**
	 * 批量删除QQ成员
	 *
	 * @param ids 需要删除的QQ成员主键
	 *
	 * @return 结果
	 */
	@Override
	public int deleteQMemberByIds(Long[] ids) {
		return qMemberMapper.deleteQMemberByIds(ids);
	}
	
	/**
	 * 删除QQ成员信息
	 *
	 * @param id QQ成员主键
	 *
	 * @return 结果
	 */
	@Override
	public int deleteQMemberById(Long id) {
		return qMemberMapper.deleteQMemberById(id);
	}
}
