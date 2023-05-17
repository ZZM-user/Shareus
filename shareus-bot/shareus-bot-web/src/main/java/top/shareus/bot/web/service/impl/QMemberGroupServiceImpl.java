package top.shareus.bot.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.domain.QMemberGroup;
import top.shareus.bot.web.mapper.QMemberGroupMapper;
import top.shareus.bot.web.service.QMemberGroupService;
import top.shareus.common.core.utils.DateUtils;

import java.util.List;

/**
 * QQ成员和群的关系Service业务层处理
 *
 * @author zhaojl
 * @date 2023-04-02
 */
@Service
public class QMemberGroupServiceImpl implements QMemberGroupService {
	@Autowired
	private QMemberGroupMapper QMemberGroupMapper;
	
	/**
	 * 查询QQ成员和群的关系
	 *
	 * @param id QQ成员和群的关系主键
	 *
	 * @return QQ成员和群的关系
	 */
	@Override
	public QMemberGroup selectQMemberGroupById(Long id) {
		return QMemberGroupMapper.selectQMemberGroupById(id);
	}
	
	/**
	 * 查询QQ成员和群的关系列表
	 *
	 * @param qMemberGroup QQ成员和群的关系
	 *
	 * @return QQ成员和群的关系
	 */
	@Override
	public List<QMemberGroup> selectQMemberGroupList(QMemberGroup qMemberGroup) {
		return QMemberGroupMapper.selectQMemberGroupList(qMemberGroup);
	}
	
	/**
	 * 新增QQ成员和群的关系
	 *
	 * @param qMemberGroup QQ成员和群的关系
	 *
	 * @return 结果
	 */
	@Override
	public int insertQMemberGroup(QMemberGroup qMemberGroup) {
		qMemberGroup.setCreateTime(DateUtils.getNowDate());
		return QMemberGroupMapper.insertQMemberGroup(qMemberGroup);
	}
	
	/**
	 * 修改QQ成员和群的关系
	 *
	 * @param qMemberGroup QQ成员和群的关系
	 *
	 * @return 结果
	 */
	@Override
	public int updateQMemberGroup(QMemberGroup qMemberGroup) {
		qMemberGroup.setUpdateTime(DateUtils.getNowDate());
		return QMemberGroupMapper.updateQMemberGroup(qMemberGroup);
	}
	
	/**
	 * 批量删除QQ成员和群的关系
	 *
	 * @param ids 需要删除的QQ成员和群的关系主键
	 *
	 * @return 结果
	 */
	@Override
	public int deleteQMemberGroupByIds(Long[] ids) {
		return QMemberGroupMapper.deleteQMemberGroupByIds(ids);
	}
	
	/**
	 * 删除QQ成员和群的关系信息
	 *
	 * @param id QQ成员和群的关系主键
	 *
	 * @return 结果
	 */
	@Override
	public int deleteQMemberGroupById(Long id) {
		return QMemberGroupMapper.deleteQMemberGroupById(id);
	}
}
