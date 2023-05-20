package top.shareus.bot.robot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.domain.ShareFileStar;

import java.util.List;

/**
 * 存档文件 Mapper
 *
 * @author 17602
 * @date 2022/11/19
 */
@Mapper
public interface ArchivedFileMapper extends BaseMapper<ArchivedFile> {
	
	/**
	 * 今天数量
	 *
	 * @return {@link Integer}
	 */
	Integer countByYesterday();
	
	/**
	 * n 之前日子
	 *
	 * @param day 一天
	 *
	 * @return {@link Integer}
	 */
	Integer countByDaysOfBefore(int day);
	
	/**
	 * 计算文件明星
	 *
	 * @param day 一天
	 *
	 * @return {@link ShareFileStar}
	 */
	List<ShareFileStar> computedFileStar(int day);
	
	/**
	 * 根据书名查询
	 *
	 * @param name 书名
	 *
	 * @return {@link List}<{@link ArchivedFile}>
	 */
	List<ArchivedFile> selectBookByName(String name);
	
	/**
	 * 通过md5 重复文件
	 *
	 * @param md5 md5
	 *
	 * @return {@link List}<{@link ArchivedFile}>
	 */
	List<ArchivedFile> selectRepeatFileByMd5(String md5);
	
	List<ArchivedFile> selectBookByNameFullText(String bookName);
}
