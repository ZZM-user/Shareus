package top.shareus.bot.web.service;


import top.shareus.bot.common.domain.QMemberGroup;

import java.util.List;

/**
 * QQ成员和群的关系Service接口
 *
 * @author zhaojl
 * @date 2023-04-02
 */
public interface QMemberGroupService {
	/**
	 * 查询QQ成员和群的关系
	 *
	 * @param id QQ成员和群的关系主键
	 *
	 * @return QQ成员和群的关系
	 */
	public QMemberGroup selectQMemberGroupById(Long id);
	
	/**
	 * 查询QQ成员和群的关系列表
	 *
	 * @param qMemberGroup QQ成员和群的关系
	 *
	 * @return QQ成员和群的关系集合
	 */
	public List<QMemberGroup> selectQMemberGroupList(QMemberGroup qMemberGroup);
	
	/**
	 * 新增QQ成员和群的关系
	 *
	 * @param qMemberGroup QQ成员和群的关系
	 *
	 * @return 结果
	 */
	public int insertQMemberGroup(QMemberGroup qMemberGroup);
	
	/**
	 * 修改QQ成员和群的关系
	 *
	 * @param qMemberGroup QQ成员和群的关系
	 *
	 * @return 结果
	 */
	public int updateQMemberGroup(QMemberGroup qMemberGroup);
	
	/**
	 * 批量删除QQ成员和群的关系
	 *
	 * @param ids 需要删除的QQ成员和群的关系主键集合
	 *
	 * @return 结果
	 */
	public int deleteQMemberGroupByIds(Long[] ids);
	
	/**
	 * 删除QQ成员和群的关系信息
	 *
	 * @param id QQ成员和群的关系主键
	 *
	 * @return 结果
	 */
	public int deleteQMemberGroupById(Long id);
}
