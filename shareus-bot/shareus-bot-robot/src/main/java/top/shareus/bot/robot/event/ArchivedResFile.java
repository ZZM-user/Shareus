package top.shareus.bot.robot.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.file.AbsoluteFile;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.FileMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.common.eumn.meilisearch.MeilisearchIndexEnums;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.bot.robot.config.MeilisearchConfig;
import top.shareus.bot.robot.mapper.ArchivedFileMapper;
import top.shareus.bot.robot.service.AlistService;
import top.shareus.bot.robot.service.QueryLogService;
import top.shareus.bot.robot.util.FileProcessor;
import top.shareus.bot.robot.util.MeilisearchUtil;
import top.shareus.bot.robot.util.MessageChainUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 转存资源群文件、并做记录
 *
 * @author 17602
 * @date 2022/11/19
 */
@Slf4j
@Component
public class ArchivedResFile extends SimpleListenerHost {
	
	/**
	 * 文件下载路径 for Linux
	 */
	public static final String FILE_DOWNLOAD_PATH = "/opt/download/mirai/groupFile/";
	
	
	@Autowired
	private ArchivedFileMapper archivedFileMapper;
	
	@Autowired
	private AlistService alistService;
	@Autowired
	private QueryLogService queryLogService;
	@Resource
	private MeilisearchConfig meilisearchConfig;
	
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.RES, GroupEnum.TEST})
	public void onArchivedResFileEvent(GroupMessageEvent event) {
		// 监听 【所有】 文件
		// 获取文件
		FileMessage fileMessage = MessageChainUtils.fetchFileMessage(event.getMessage());
		
		if (! System.getProperty("os.name").contains("Windows") || ObjectUtil.isNull(fileMessage)) {
			return;
		}
		
		// 下载文件
		ArchivedFile archivedFile = downloadFile(event, fileMessage);
		if (ObjectUtil.isNull(archivedFile)) {
			return;
		}
		
		// 转存文件，存放盘，获取连接
		log.info("文件下载成功：{}", archivedFile.getName());
		File file = new File(archivedFile.getArchiveUrl());
		
		FileProcessor.insertWatermark(file, 3);
		log.info("归档路径：{}", archivedFile.getArchiveUrl());
		
		CompletableFuture.runAsync(() -> {
			String uploadFilePath = alistService.uploadFile(file);
			if (CharSequenceUtil.isNotBlank(uploadFilePath)) {
				archivedFile.setArchiveUrl(uploadFilePath);
				log.info(archivedFile.toString());
				// 将信息 写入数据库
				archivedFileMapper.insert(archivedFile);
				MeilisearchUtil.addDocuments(meilisearchConfig.getClient(), MeilisearchIndexEnums.ARCHIVED_FILE, Collections.singletonList(archivedFile));
				// 判断 是否完成求文
				queryLogService.queryLogByBookName(archivedFile);
			}
		}).whenComplete((r, e) -> {
			if (e != null) {
				log.error("归档文件异常: {}", file.getName(), e);
				log.info("{} 存档失败！", archivedFile);
			} else {
				log.info("{} 存档完成！", archivedFile.getName());
			}
		});
	}
	
	/**
	 * 下载文件
	 *
	 * @param event       事件
	 * @param fileMessage 文件信息
	 *
	 * @return {@code ArchivedFile}
	 */
	private ArchivedFile downloadFile(GroupMessageEvent event, FileMessage fileMessage) {
		AbsoluteFile file = fileMessage.toAbsoluteFile(event.getGroup());
		// 构造 文件归档url
		String archiveUrl = FILE_DOWNLOAD_PATH + fileMessage.getName();
		// 下载文件
		long len = HttpUtil.downloadFile(file.getUrl(), new File(archiveUrl));
		
		log.info("{}: len = {}", fileMessage.getName(), len);
		
		if (len > 0) {
			// 碰撞先关
			String md5 = String.valueOf(ByteUtil.bytesToLong(file.getMd5()));
			
			List<ArchivedFile> archivedFiles = archivedFileMapper.selectRepeatFileByMd5(md5);
			
			ArchivedFile archivedFile = new ArchivedFile();
			archivedFile.setId(IdUtil.simpleUUID());
			archivedFile.setName(file.getName());
			archivedFile.setSenderId(event.getSender().getId());
			archivedFile.setSize(file.getSize());
			archivedFile.setMd5(md5);
			archivedFile.setEnabled(CollUtil.isEmpty(archivedFiles) ? 0 : 1);
			archivedFile.setOriginUrl(file.getUrl());
			archivedFile.setArchiveUrl(archiveUrl);
			archivedFile.setArchiveDate(new Date());
			
			return archivedFile;
		}
		return null;
	}
	
	@Override
	public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
		log.error("{}\n{}\n{}", context, exception.getMessage(), exception.getCause().getMessage());
	}
}
