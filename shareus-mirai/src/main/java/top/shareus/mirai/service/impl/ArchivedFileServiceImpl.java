package top.shareus.mirai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.mirai.domain.ArchivedFile;
import top.shareus.mirai.mapper.ArchivedFileMapper;
import top.shareus.mirai.service.IArchivedFileService;
import top.shareus.mirai.vo.BatchChangeStatusVO;

import java.util.List;

/**
 * 机器人Service业务层处理
 *
 * @author zhaojl
 * @date 2023-01-09
 */
@Service
public class ArchivedFileServiceImpl extends ServiceImpl<ArchivedFileMapper, ArchivedFile> implements IArchivedFileService {
    @Autowired
    private ArchivedFileMapper archivedFileMapper;

    /**
     * 查询机器人
     *
     * @param id 机器人主键
     * @return 机器人
     */
    @Override
    public ArchivedFile selectArchivedFileById(String id) {
        return archivedFileMapper.selectArchivedFileById(id);
    }

    /**
     * 查询机器人列表
     *
     * @param archivedFile 机器人
     * @return 机器人
     */
    @Override
    public List<ArchivedFile> selectArchivedFileList(ArchivedFile archivedFile) {
        return archivedFileMapper.selectArchivedFileList(archivedFile);
    }

    /**
     * 新增机器人
     *
     * @param archivedFile 机器人
     * @return 结果
     */
    @Override
    public int insertArchivedFile(ArchivedFile archivedFile) {
        return archivedFileMapper.insertArchivedFile(archivedFile);
    }

    /**
     * 修改机器人
     *
     * @param archivedFile 机器人
     * @return 结果
     */
    @Override
    public int updateArchivedFile(ArchivedFile archivedFile) {
        return archivedFileMapper.updateArchivedFile(archivedFile);
    }

    /**
     * 批量删除机器人
     *
     * @param ids 需要删除的机器人主键
     * @return 结果
     */
    @Override
    public int deleteArchivedFileByIds(String[] ids) {
        return archivedFileMapper.deleteArchivedFileByIds(ids);
    }

    /**
     * 删除机器人信息
     *
     * @param id 机器人主键
     * @return 结果
     */
    @Override
    public int deleteArchivedFileById(String id) {
        return archivedFileMapper.deleteArchivedFileById(id);
    }

    @Override
    public int batchChangeStatus(BatchChangeStatusVO vo) {
        List<ArchivedFile> archivedFiles = this.listByIds(vo.getStringIds());
        archivedFiles.forEach(archivedFile -> archivedFile.setEnabled(vo.getStatus()));
        this.updateBatchById(archivedFiles);
        return archivedFiles.size();
    }
}
