package top.shareus.bot.robot.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.robot.mapper.ArchivedFileMapper;
import top.shareus.bot.robot.service.ArchivedFileService;

import java.util.Arrays;
import java.util.List;

/**
 * 归档文件服务
 *
 * @author zhaojl
 * @date 2023/01/24
 */
@Slf4j
@Service
public class ArchivedFileServiceImpl extends ServiceImpl<ArchivedFileMapper, ArchivedFile> implements ArchivedFileService {
	
	@Autowired
	private ArchivedFileMapper archivedFileMapper;
	
	
	@Override
	public List<ArchivedFile> findBookInfoByName(String bookName) {
		if (StrUtil.isBlank(bookName)) {
			return null;
		}
		
		List<ArchivedFile> archivedFiles = archivedFileMapper.selectBookByName(bookName);
		if (CollUtil.isEmpty(archivedFiles)) {
			log.info("查不到相关内容");
			return null;
		}
		
		log.info("查询到名为：" + bookName + " 的书目，共：" + archivedFiles.size() + " 条\n" + Arrays.toString(archivedFiles.toArray()));
		return archivedFiles;
	}
}
