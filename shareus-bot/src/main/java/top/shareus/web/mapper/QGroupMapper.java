package top.shareus.web.mapper;


import top.shareus.domain.entity.QGroup;

import java.util.List;

/**
 * QQ群组Mapper接口
 *
 * @author zhaojl
 * @date 2023-04-02
 */
public interface QGroupMapper {
    /**
     * 查询QQ群组
     *
     * @param id QQ群组主键
     *
     * @return QQ群组
     */
    public QGroup selectQGroupById(Long id);
    
    /**
     * 查询QQ群组列表
     *
     * @param qGroup QQ群组
     *
     * @return QQ群组集合
     */
    public List<QGroup> selectQGroupList(QGroup qGroup);
    
    /**
     * 新增QQ群组
     *
     * @param qGroup QQ群组
     *
     * @return 结果
     */
    public int insertQGroup(QGroup qGroup);
    
    /**
     * 修改QQ群组
     *
     * @param qGroup QQ群组
     *
     * @return 结果
     */
    public int updateQGroup(QGroup qGroup);
    
    /**
     * 删除QQ群组
     *
     * @param id QQ群组主键
     *
     * @return 结果
     */
    public int deleteQGroupById(Long id);
    
    /**
     * 批量删除QQ群组
     *
     * @param ids 需要删除的数据主键集合
     *
     * @return 结果
     */
    public int deleteQGroupByIds(Long[] ids);
}
