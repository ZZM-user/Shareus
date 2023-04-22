package top.shareus.bot.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;
import top.shareus.bot.config.BotManager;
import top.shareus.common.core.constant.GroupsConstant;
import top.shareus.common.core.constant.QiuWenConstant;
import top.shareus.common.redis.service.RedisService;
import top.shareus.common.reids.RedisClient;

import java.util.concurrent.TimeUnit;

/**
 * 查询文件存档 工具类
 *
 * @author zhaojl
 * @date 2023/01/08
 */
@Slf4j
@Component
public class QueryArchivedResFileUtils {

    private static RedisClient redisClient = SpringUtil.getBean(RedisClient.class);
    private static RedisService redisService = SpringUtil.getBean(RedisService.class);

    /**
     * 检查 求文次数是否正常
     *
     * @param event 事件
     * @return boolean
     */
    public static boolean checkWarring(Long senderId, String nickName) {
        String key = QiuWenConstant.QIU_WEN_REDIS_KEY + senderId;
        int times = QueryArchivedResFileUtils.getTimes(key);

        if (times > QiuWenConstant.MAX_TIMES_OF_DAY) {
            Bot bot = BotManager.getBot();
            Group group = bot.getGroup(GroupsConstant.ADMIN_GROUPS.get(0));
            group.sendMessage("请注意 \n[" + senderId + nickName + "]\n该用户今日已求文第 " + times + " 次");
            MuteUtils.mute(group, senderId, QiuWenConstant.getExpireTime());
            return true;
        }

        return false;
    }

    /**
     * 检查模板错误
     *
     * @param senderId   发件人id
     * @param senderName 发送者名字
     */
    public static void checkTemplateError(long senderId, String senderName) {
        String key = QiuWenConstant.ERROR_QIU_WEN_TEMPLATE_REDIS_KEY + senderId;
        int times = QueryArchivedResFileUtils.incrTimes(key, QiuWenConstant.getExpireTimeOfErrorTemplate());

        if (times >= QiuWenConstant.ERROR_TEMPLATE_MAX_TIMES_OF_WEEK) {
            Bot bot = BotManager.getBot();
            Group group = bot.getGroup(GroupsConstant.ADMIN_GROUPS.get(0));
            group.sendMessage("请注意 \n发现 【" + senderName + "】(" + senderId + ")]\n该用户本周第 " + times + " 次，求文规范错误");
            MuteUtils.mute(group, senderId, QiuWenConstant.getExpireTimeOfErrorTemplate());
        }
    }

    /**
     * 是求文
     *
     * @param plainText 纯文本
     * @return boolean
     */
    public static boolean isQiuWen(PlainText plainText) {
        if (ObjectUtil.isNull(plainText)) {
            return false;
        }

        String content = plainText.getContent();
        if (content.length() > 50) {
            log.info("这哪是求文啊，发公告呢吧…… " + content.length());
            return false;
        }

        return ReUtil.contains("(书名)", content) && ReUtil.contains("(作者)", content) && ReUtil.contains("(平台)", content);
    }

    /**
     * 提取书信息
     *
     * @param plainText 纯文本
     * @return {@code String}
     */
    public static String extractBookInfo(PlainText plainText) {
        if (ObjectUtil.isNull(plainText)) {
            return "";
        }

        String content = plainText.getContent();
        if (!content.startsWith("书名")) {
            return "";
        }

        if (StrUtil.contains(content, "晋江")) {
            return "晋江";
        }

        // 新规则
        // 书名：静夜思\n作者：李白\n平台：未知
        String[] split = content.split("\n");
        String result = StrUtil.removePrefix(split[0], "书名");
        result = result
                .replace("：", "")
                .replace(":", "")
                .replace("《", "")
                .replace("》", "")
                .trim();
        result = StrUtil.cleanBlank(result);

        // 去除 [类型]
        int indexOf = result.indexOf("[");
        if (indexOf != -1) {
            int indexOf1 = result.indexOf("]");
            result = StrUtil.replace(result, indexOf, indexOf1 + 1, "");
        }

        // 太长折半
        if (result.length() > 30) {
            result = result.substring(0, result.length() / 2);
        }

        log.info("求文拆分结果：" + result);

        return result;
    }

    /**
     * 获取求文次数
     *
     * @return int
     */
    public static int getTimes(String key) {
        int times = redisService.getCacheObject(key);
        return ObjectUtil.isNotNull(times) ? times : 0;
    }

    /**
     * 增加次数
     */
    public static int incrTimes(String key, long expire) {
        int oldValue = redisService.getCacheObject(key);
        if (ObjectUtil.isNull(oldValue)) {
            redisService.setCacheObject(key, 0, expire, TimeUnit.SECONDS);
        }
        redisClient.increment(key, 1);
        return redisService.getCacheObject(key);
    }

    /**
     * 消减次数
     */
    public static void decrTimes(String key, long expire) {
        int oldValue = redisService.getCacheObject(key);
        if (ObjectUtil.isNotNull(oldValue)) {
            redisClient.increment(key, -1);
        } else {
            redisService.setCacheObject(key, 0, expire, TimeUnit.SECONDS);
        }
    }
}