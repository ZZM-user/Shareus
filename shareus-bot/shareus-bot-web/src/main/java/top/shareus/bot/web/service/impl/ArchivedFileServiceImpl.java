package top.shareus.bot.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.pojo.vo.BatchChangeStatusVO;
import top.shareus.bot.web.mapper.ArchivedFileMapper;
import top.shareus.bot.web.service.ArchivedFileService;

import java.util.List;

/**
 * 归档文件Service业务层处理
 *
 * @author zhaojl
 * @date 2023-01-09
 */
@Service
public class ArchivedFileServiceImpl extends ServiceImpl<ArchivedFileMapper, ArchivedFile> implements ArchivedFileService {
	@Autowired
	private ArchivedFileMapper ArchivedFileMapper;
	
	/**
	 * 查询归档文件
	 *
	 * @param id 归档文件主键
	 *
	 * @return 归档文件
	 */
	@Override
	public ArchivedFile selectArchivedFileById(String id) {
		return ArchivedFileMapper.selectArchivedFileById(id);
	}
	
	/**
	 * 查询归档文件列表
	 *
	 * @param archivedFile 归档文件
	 *
	 * @return 归档文件
	 */
	@Override
	public List<ArchivedFile> selectArchivedFileList(ArchivedFile archivedFile) {
		return ArchivedFileMapper.selectArchivedFileList(archivedFile);
	}
	
	/**
	 * 新增归档文件
	 *
	 * @param archivedFile 归档文件
	 *
	 * @return 结果
	 */
	@Override
	public int insertArchivedFile(ArchivedFile archivedFile) {
		return ArchivedFileMapper.insertArchivedFile(archivedFile);
	}
	
	/**
	 * 修改归档文件
	 *
	 * @param archivedFile 归档文件
	 *
	 * @return 结果
	 */
	@Override
	public int updateArchivedFile(ArchivedFile archivedFile) {
		return ArchivedFileMapper.updateArchivedFile(archivedFile);
	}
	
	/**
	 * 批量删除归档文件
	 *
	 * @param ids 需要删除的归档文件主键
	 *
	 * @return 结果
	 */
	@Override
	public int deleteArchivedFileByIds(String[] ids) {
		return ArchivedFileMapper.deleteArchivedFileByIds(ids);
	}
	
	/**
	 * 删除归档文件信息
	 *
	 * @param id 归档文件主键
	 *
	 * @return 结果
	 */
	@Override
	public int deleteArchivedFileById(String id) {
		return ArchivedFileMapper.deleteArchivedFileById(id);
	}
	
	@Override
	public int batchChangeStatus(BatchChangeStatusVO vo) {
		List<ArchivedFile> archivedFiles = this.listByIds(vo.getStringIds());
		archivedFiles.forEach(archivedFile -> archivedFile.setEnabled(vo.getStatus()));
		this.updateBatchById(archivedFiles);
		return archivedFiles.size();
	}
}
