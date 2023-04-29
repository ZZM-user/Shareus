package top.shareus.bot.event;

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
import top.shareus.bot.annotation.GroupAuth;
import top.shareus.bot.service.ArchivedFileService;
import top.shareus.bot.util.MessageChainUtils;
import top.shareus.bot.util.QueryArchivedResFileUtils;
import top.shareus.bot.util.QueryLogUtils;
import top.shareus.bot.util.ShortUrlUtils;
import top.shareus.common.core.constant.QiuWenConstant;
import top.shareus.common.core.eumn.GroupEnum;
import top.shareus.domain.entity.ArchivedFile;

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
	
	@EventHandler
	@GroupAuth(groupList = {GroupEnum.RES_GROUP, GroupEnum.TEST_GROUP})
	public void onQueryArchivedResFile(GroupMessageEvent event) {
		long senderId = event.getSender().getId();
		
		try {
			PlainText plainText = MessageChainUtils.fetchPlainText(event.getMessage());
			// 不包含 求文 不管
			if (! QueryArchivedResFileUtils.isQiuWen(plainText)) {
				return;
			}
			
			if (QueryArchivedResFileUtils.checkWarring(senderId, event.getSenderName())) {
				log.error(event.getSender().getNameCard() + "/" + senderId + " 求文次数异常！");
				return;
			}
			
			// 提取书名
			String bookName = QueryArchivedResFileUtils.extractBookInfo(plainText);
			
			// 规范错误
			if (StrUtil.isEmpty(bookName)) {
				log.info("求文规范错误！");
				MessageSource.recall(event.getMessage());
				QueryArchivedResFileUtils.checkTemplateError(senderId, event.getSenderName());
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
			QueryLogUtils.recordLog(event, plainText.getContent(), bookName, archivedFiles);
			
			if (CollUtil.isEmpty(archivedFiles)) {
				log.info("没查到关于 [" + bookName + "] 的库存信息");
				return;
			}
			
			// 求文次数 + 1
			String key = QiuWenConstant.QIU_WEN_REDIS_KEY + senderId;
			QueryArchivedResFileUtils.incrTimes(key, QiuWenConstant.getExpireTime());
			
			// 查到了书目信息 构建消息链
			MessageChainBuilder builder = new MessageChainBuilder();
			builder.add(new At(senderId));
			builder.add("\n小度为你找到了以下内容：");
			
			archivedFiles.forEach(a ->
										  builder.add("\n名称：" + a.getName() + "\n" + "下载地址：" + ShortUrlUtils.generateShortUrl(a.getArchiveUrl()))
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
