package top.shareus.mirai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.mirai.domain.QueryLog;
import top.shareus.mirai.mapper.QueryLogMapper;
import top.shareus.mirai.service.IQueryLogService;
import top.shareus.mirai.vo.BatchChangeStatusVO;

import java.util.List;

/**
 * 求文日志Service业务层处理
 *
 * @author zhaojl
 * @date 2023-01-09
 */
@Service
public class QueryLogServiceImpl extends ServiceImpl<QueryLogMapper, QueryLog> implements IQueryLogService {
    @Autowired
    private QueryLogMapper queryLogMapper;

    /**
     * 查询求文日志
     *
     * @param id 求文日志主键
     * @return 求文日志
     */
    @Override
    public QueryLog selectQueryLogById(Long id) {
        return queryLogMapper.selectById(id);
    }

    /**
     * 查询求文日志列表
     *
     * @param queryLog 求文日志
     * @return 求文日志
     */
    @Override
    public List<QueryLog> selectQueryLogList(QueryLog queryLog) {
        return queryLogMapper.selectQueryLogList(queryLog);
    }

    /**
     * 新增求文日志
     *
     * @param queryLog 求文日志
     * @return 结果
     */
    @Override
    public int insertQueryLog(QueryLog queryLog) {
        return queryLogMapper.insertQueryLog(queryLog);
    }

    /**
     * 修改求文日志
     *
     * @param queryLog 求文日志
     * @return 结果
     */
    @Override
    public int updateQueryLog(QueryLog queryLog) {
        return queryLogMapper.updateQueryLog(queryLog);
    }

    /**
     * 批量删除求文日志
     *
     * @param ids 需要删除的求文日志主键
     * @return 结果
     */
    @Override
    public int deleteQueryLogByIds(Long[] ids) {
        return queryLogMapper.deleteQueryLogByIds(ids);
    }

    /**
     * 删除求文日志信息
     *
     * @param id 求文日志主键
     * @return 结果
     */
    @Override
    public int deleteQueryLogById(Long id) {
        return queryLogMapper.deleteQueryLogById(id);
    }

    @Override
    public int batchChangeStatus(BatchChangeStatusVO vo) {
        List<QueryLog> queryLogs = this.listByIds(vo.getIds());
        queryLogs.forEach(queryLog -> queryLog.setStatus(vo.getStatus()));
        this.updateBatchById(queryLogs);
        return queryLogs.size();
    }
}
