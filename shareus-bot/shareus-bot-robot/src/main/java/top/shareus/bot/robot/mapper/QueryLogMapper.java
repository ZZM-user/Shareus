package top.shareus.bot.robot.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.shareus.bot.common.domain.QueryLog;

import java.util.List;
import java.util.Map;

/**
 * 查询日志映射器
 *
 * @author zhaojl
 * @date 2023/01/07
 */
@Mapper
public interface QueryLogMapper extends BaseMapper<QueryLog> {
	
	/**
	 * 未完成的查询
	 *
	 * @return {@link List}<{@link QueryLog}>
	 */
	List<QueryLog> selectAllUnfinishedQuery();
	
	/**
	 * 查询日志 书名
	 *
	 * @param bookName 书名
	 *
	 * @return {@link List}<{@link QueryLog}>
	 */
	List<QueryLog> queryLogByBookName(String bookName);
	
	/**
	 * 选择未完成查询
	 *
	 * @param day 一天
	 *
	 * @return {@link List}<{@link QueryLog}>
	 */
	List<QueryLog> selectUnfinishedQuery(Integer day);
	
	/**
	 * 昨天 求问数量
	 *
	 * @return {@link List}<{@link QueryLog}>
	 */
	Integer countByYesterday();
	
	/**
	 * 根据发送者信息查询未完成求文
	 *
	 * @param id   id
	 * @param date 日期
	 *
	 * @return {@link List}<{@link QueryLog}>
	 */
	List<QueryLog> selectUnfinishedQueryBySender(@Param("senderId") long id, @Param("sendTime") DateTime date);
	
	Map<Long, QueryLog> selectOfNDayHotQueryRank(int days);
}
