package top.shareus.mirai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.shareus.mirai.domain.ArchivedFile;

import java.util.List;

/**
 * 机器人Mapper接口
 *
 * @author zhaojl
 * @date 2023-01-09
 */
public interface ArchivedFileMapper extends BaseMapper<ArchivedFile> {
    /**
     * 查询机器人
     *
     * @param id 机器人主键
     * @return 机器人
     */
    public ArchivedFile selectArchivedFileById(String id);

    /**
     * 查询机器人列表
     *
     * @param archivedFile 机器人
     * @return 机器人集合
     */
    public List<ArchivedFile> selectArchivedFileList(ArchivedFile archivedFile);

    /**
     * 新增机器人
     *
     * @param archivedFile 机器人
     * @return 结果
     */
    public int insertArchivedFile(ArchivedFile archivedFile);

    /**
     * 修改机器人
     *
     * @param archivedFile 机器人
     * @return 结果
     */
    public int updateArchivedFile(ArchivedFile archivedFile);

    /**
     * 删除机器人
     *
     * @param id 机器人主键
     * @return 结果
     */
    public int deleteArchivedFileById(String id);

    /**
     * 批量删除机器人
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteArchivedFileByIds(String[] ids);
}
