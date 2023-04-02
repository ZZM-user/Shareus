package top.shareus.web.service;


import top.shareus.domain.entity.QMember;

import java.util.List;

/**
 * QQ成员Service接口
 *
 * @author zhaojl
 * @date 2023-04-02
 */
public interface IQMemberService {
    /**
     * 查询QQ成员
     *
     * @param id QQ成员主键
     *
     * @return QQ成员
     */
    public QMember selectQMemberById(Long id);
    
    /**
     * 查询QQ成员列表
     *
     * @param qMember QQ成员
     *
     * @return QQ成员集合
     */
    public List<QMember> selectQMemberList(QMember qMember);
    
    /**
     * 新增QQ成员
     *
     * @param qMember QQ成员
     *
     * @return 结果
     */
    public int insertQMember(QMember qMember);
    
    /**
     * 修改QQ成员
     *
     * @param qMember QQ成员
     *
     * @return 结果
     */
    public int updateQMember(QMember qMember);
    
    /**
     * 批量删除QQ成员
     *
     * @param ids 需要删除的QQ成员主键集合
     *
     * @return 结果
     */
    public int deleteQMemberByIds(Long[] ids);
    
    /**
     * 删除QQ成员信息
     *
     * @param id QQ成员主键
     *
     * @return 结果
     */
    public int deleteQMemberById(Long id);
}
