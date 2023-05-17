package top.shareus.bot.robot.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.file.AbsoluteFile;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.FileMessage;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.bot.robot.mapper.ArchivedFileMapper;
import top.shareus.bot.robot.util.AlistUtils;
import top.shareus.bot.robot.util.MessageChainUtils;
import top.shareus.bot.robot.util.QueryLogUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

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
	ArchivedFileMapper archivedFileMapper;
	
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.RES, GroupEnum.TEST})
	public void onArchivedResFileEvent(GroupMessageEvent event) {
		// 监听 【所有】 文件
		MessageChain message = event.getMessage();
		// 获取文件
		FileMessage fileMessage = MessageChainUtils.fetchFileMessage(message);
		
		if (ObjectUtil.isNotNull(fileMessage)) {
			// 下载文件
			ArchivedFile archivedFile = downloadFile(event, fileMessage);
			
			if (ObjectUtil.isNotNull(archivedFile)) {
				// 转存文件，存放盘，获取连接
				log.info("文件下载成功：" + archivedFile.getName());
				File file = new File(archivedFile.getArchiveUrl());
				
				log.info("归档路径：" + archivedFile.getArchiveUrl());
				
				try {
					String uploadFilePath = AlistUtils.uploadFile(file);
					if (StrUtil.isNotBlank(uploadFilePath)) {
						archivedFile.setArchiveUrl(uploadFilePath);
						log.info(archivedFile.toString());
						// 将信息 写入数据库
						archivedFileMapper.insert(archivedFile);
						// 判断 是否完成求文
						QueryLogUtils.queryLogByBookName(archivedFile);
					}
					log.info(archivedFile.getName() + " 存档完成！");
					return;
				} catch (Exception e) {
					log.error("归档文件异常", e);
				}
			}
			log.info(archivedFile + " 存档失败！");
		}
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
		
		log.info(fileMessage.getName() + ": len = " + len);
		
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
		log.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
	}
}
