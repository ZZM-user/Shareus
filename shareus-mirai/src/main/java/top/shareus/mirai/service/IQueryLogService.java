package top.shareus.mirai.service;

import top.shareus.mirai.domain.QueryLog;

import java.util.List;

/**
 * 求文日志Service接口
 *
 * @author zhaojl
 * @date 2023-01-09
 */
public interface IQueryLogService {
    /**
     * 查询求文日志
     *
     * @param id 求文日志主键
     * @return 求文日志
     */
    public QueryLog selectQueryLogById(Long id);

    /**
     * 查询求文日志列表
     *
     * @param queryLog 求文日志
     * @return 求文日志集合
     */
    public List<QueryLog> selectQueryLogList(QueryLog queryLog);

    /**
     * 新增求文日志
     *
     * @param queryLog 求文日志
     * @return 结果
     */
    public int insertQueryLog(QueryLog queryLog);

    /**
     * 修改求文日志
     *
     * @param queryLog 求文日志
     * @return 结果
     */
    public int updateQueryLog(QueryLog queryLog);

    /**
     * 批量删除求文日志
     *
     * @param ids 需要删除的求文日志主键集合
     * @return 结果
     */
    public int deleteQueryLogByIds(Long[] ids);

    /**
     * 删除求文日志信息
     *
     * @param id 求文日志主键
     * @return 结果
     */
    public int deleteQueryLogById(Long id);
}
