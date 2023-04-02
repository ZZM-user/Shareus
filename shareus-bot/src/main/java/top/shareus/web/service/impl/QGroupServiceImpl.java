package top.shareus.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.common.core.utils.DateUtils;
import top.shareus.domain.entity.QGroup;
import top.shareus.web.mapper.QGroupMapper;
import top.shareus.web.service.IQGroupService;

import java.util.List;

/**
 * QQ群组Service业务层处理
 *
 * @author zhaojl
 * @date 2023-04-02
 */
@Service
public class QGroupServiceImpl implements IQGroupService {
    @Autowired
    private QGroupMapper qGroupMapper;
    
    /**
     * 查询QQ群组
     *
     * @param id QQ群组主键
     *
     * @return QQ群组
     */
    @Override
    public QGroup selectQGroupById(Long id) {
        return qGroupMapper.selectQGroupById(id);
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
        return qGroupMapper.selectQGroupList(qGroup);
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
        return qGroupMapper.insertQGroup(qGroup);
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
        return qGroupMapper.updateQGroup(qGroup);
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
        return qGroupMapper.deleteQGroupByIds(ids);
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
        return qGroupMapper.deleteQGroupById(id);
    }
}
