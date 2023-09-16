package top.shareus.bot.robot.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.constant.QiuWenConstant;
import top.shareus.bot.common.domain.ArchivedFile;
import top.shareus.bot.common.eumn.bot.GroupEnum;
import top.shareus.bot.robot.annotation.GroupAuth;
import top.shareus.bot.robot.service.ArchivedFileService;
import top.shareus.bot.robot.service.QueryArchivedResFileService;
import top.shareus.bot.robot.service.QueryLogService;
import top.shareus.bot.robot.util.MessageChainUtils;
import top.shareus.bot.robot.util.ShortUrlUtils;

import java.util.List;

/**
 * 查询归档res文件
 *
 * @author 17602
 * @date 2022/11/20
 */
@Slf4j
@Component
public class QueryArchivedResFile extends SimpleListenerHost {
	
	@Autowired
	private ArchivedFileService archivedFileService;
	@Autowired
	private QueryArchivedResFileService queryArchivedResFileService;
	@Autowired
	private QueryLogService queryLogService;
	
	@EventHandler
	@GroupAuth(allowGroupList = {GroupEnum.RES, GroupEnum.TEST})
	public void onQueryArchivedResFile(GroupMessageEvent event) {
		long senderId = event.getSender().getId();
		
		try {
			PlainText plainText = MessageChainUtils.fetchPlainText(event.getMessage());
			// 不包含 求文 不管
			if (! queryArchivedResFileService.isQiuWen(plainText)) {
				return;
			}
			
			if (queryArchivedResFileService.checkWarring(senderId, event.getSenderName())) {
				log.error(event.getSender().getNameCard() + "/" + senderId + " 求文次数异常！");
				return;
			}
			
			
			// 提取书名
			String bookName = queryArchivedResFileService.extractBookInfo(plainText);
			
			// 规范错误
			if (StrUtil.isEmpty(bookName)) {
				log.info("求文规范错误！");
				MessageSource.recall(event.getMessage());
				queryArchivedResFileService.checkTemplateError(senderId, event.getSenderName());
				MessageChainBuilder builder = new MessageChainBuilder();
				builder.add(new At(event.getSender().getId()));
				builder.add("\n求文规范错误！详情见群公告");
				event.getGroup().sendMessage(builder.build());
				return;
			}
			
			if ("晋江".equals(bookName)) {
				log.info("检测到晋江作品求文！\t准备撤回");
				MessageSource.recall(event.getMessage());
				return;
			}
			
			// 查询
			List<ArchivedFile> archivedFiles = archivedFileService.findBookInfoByName(bookName);
			
			// 求文记录
			queryLogService.recordLog(event, plainText.getContent(), bookName, archivedFiles);
			
			if (CollUtil.isEmpty(archivedFiles)) {
				log.info("没查到关于 [" + bookName + "] 的库存信息");
				return;
			}
			
			// 求文次数 + 1
			String key = QiuWenConstant.QIU_WEN_REDIS_KEY + senderId;
			queryArchivedResFileService.incrTimes(key, QiuWenConstant.getExpireTime());
			
			// 查到了书目信息 构建消息链
			MessageChainBuilder builder = new MessageChainBuilder();
			builder.add(new At(senderId));
			builder.add("\n小度为你找到了以下内容：");
			
			archivedFiles.forEach(a ->
								  {
									  // 这里等域名恢复了需要删除
									  String newUrl = a.getArchiveUrl().replace("https://pan.shareus.top", "http://124.220.67.51:5244/");
									  builder.add("\n名称：" + a.getName() + "\n" + "下载地址：" + ShortUrlUtils.generateShortUrl(newUrl));
								  }
								 );
			
			event.getGroup().sendMessage(builder.build());
		} catch (Exception e) {
			log.error("求文异常", e);
		}
	}
	
	@Override
	public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
		log.error(context + "\n" + exception.getMessage() + "\n" + exception.getCause().getMessage());
	}
}
