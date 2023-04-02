package top.shareus.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.common.core.utils.DateUtils;
import top.shareus.domain.entity.QMemberGroup;
import top.shareus.web.mapper.QMemberGroupMapper;
import top.shareus.web.service.IQMemberGroupService;

import java.util.List;

/**
 * QQ成员和群的关系Service业务层处理
 *
 * @author zhaojl
 * @date 2023-04-02
 */
@Service
public class QMemberGroupServiceImpl implements IQMemberGroupService {
    @Autowired
    private QMemberGroupMapper qMemberGroupMapper;
    
    /**
     * 查询QQ成员和群的关系
     *
     * @param id QQ成员和群的关系主键
     *
     * @return QQ成员和群的关系
     */
    @Override
    public QMemberGroup selectQMemberGroupById(Long id) {
        return qMemberGroupMapper.selectQMemberGroupById(id);
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
        return qMemberGroupMapper.selectQMemberGroupList(qMemberGroup);
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
        return qMemberGroupMapper.insertQMemberGroup(qMemberGroup);
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
        return qMemberGroupMapper.updateQMemberGroup(qMemberGroup);
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
        return qMemberGroupMapper.deleteQMemberGroupByIds(ids);
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
        return qMemberGroupMapper.deleteQMemberGroupById(id);
    }
}
