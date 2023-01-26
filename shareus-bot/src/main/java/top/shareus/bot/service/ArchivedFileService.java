package top.shareus.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import top.shareus.domain.entity.ArchivedFile;

import java.util.List;

/**
 * 归档文件服务
 *
 * @author zhaojl
 * @date 2023/01/24
 */
@Mapper
public interface ArchivedFileService extends IService<ArchivedFile> {

    /**
     * 根据书名查询信息
     *
     * @param bookName 书名字
     * @return {@link List}<{@link ArchivedFile}>
     */
    public List<ArchivedFile> findBookInfoByName(String bookName);
}
