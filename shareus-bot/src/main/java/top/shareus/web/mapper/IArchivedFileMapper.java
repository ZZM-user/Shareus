package top.shareus.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.shareus.domain.entity.ArchivedFile;

import java.util.List;

/**
 * 归档文件Mapper接口
 *
 * @author zhaojl
 * @date 2023-01-09
 */
public interface IArchivedFileMapper extends BaseMapper<ArchivedFile> {
    /**
     * 查询归档文件
     *
     * @param id 归档文件主键
     * @return 归档文件
     */
    public ArchivedFile selectArchivedFileById(String id);

    /**
     * 查询归档文件列表
     *
     * @param archivedFile 归档文件
     * @return 归档文件集合
     */
    public List<ArchivedFile> selectArchivedFileList(ArchivedFile archivedFile);

    /**
     * 新增归档文件
     *
     * @param archivedFile 归档文件
     * @return 结果
     */
    public int insertArchivedFile(ArchivedFile archivedFile);

    /**
     * 修改归档文件
     *
     * @param archivedFile 归档文件
     * @return 结果
     */
    public int updateArchivedFile(ArchivedFile archivedFile);

    /**
     * 删除归档文件
     *
     * @param id 归档文件主键
     * @return 结果
     */
    public int deleteArchivedFileById(String id);

    /**
     * 批量删除归档文件
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteArchivedFileByIds(String[] ids);
}
