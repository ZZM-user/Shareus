package top.shareus.bot.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.domain.QGroup;
import top.shareus.bot.web.mapper.QGroupMapper;
import top.shareus.bot.web.service.QGroupService;
import top.shareus.common.core.utils.DateUtils;

import java.util.List;

/**
 * QQ群组Service业务层处理
 *
 * @author zhaojl
 * @date 2023-04-02
 */
@Service
public class QGroupServiceImpl implements QGroupService {
	@Autowired
	private QGroupMapper QGroupMapper;
	
	/**
	 * 查询QQ群组
	 *
	 * @param id QQ群组主键
	 *
	 * @return QQ群组
	 */
	@Override
	public QGroup selectQGroupById(Long id) {
		return QGroupMapper.selectQGroupById(id);
	}
	
	/**
	 * 查询QQ群组列表
	 *
	 * @param qGroup QQ群组
	 *
	 * @return QQ群组
	 */
	@Override
	public List<QGroup> selectQGroupList(QGroup qGroup) {
		return QGroupMapper.selectQGroupList(qGroup);
	}
	
	/**
	 * 新增QQ群组
	 *
	 * @param qGroup QQ群组
	 *
	 * @return 结果
	 */
	@Override
	public int insertQGroup(QGroup qGroup) {
		qGroup.setCreateTime(DateUtils.getNowDate());
		return QGroupMapper.insertQGroup(qGroup);
	}
	
	/**
	 * 修改QQ群组
	 *
	 * @param qGroup QQ群组
	 *
	 * @return 结果
	 */
	@Override
	public int updateQGroup(QGroup qGroup) {
		qGroup.setUpdateTime(DateUtils.getNowDate());
		return QGroupMapper.updateQGroup(qGroup);
	}
	
	/**
	 * 批量删除QQ群组
	 *
	 * @param ids 需要删除的QQ群组主键
	 *
	 * @return 结果
	 */
	@Override
	public int deleteQGroupByIds(Long[] ids) {
		return QGroupMapper.deleteQGroupByIds(ids);
	}
	
	/**
	 * 删除QQ群组信息
	 *
	 * @param id QQ群组主键
	 *
	 * @return 结果
	 */
	@Override
	public int deleteQGroupById(Long id) {
		return QGroupMapper.deleteQGroupById(id);
	}
}
