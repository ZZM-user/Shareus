package top.shareus.bot.api.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.shareus.bot.api.RemoteBotJobService;
import top.shareus.common.core.domain.R;

/**
 * 文件服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteBotJobService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteFileFallbackFactory.class);

    @Override
    public RemoteBotJobService create(Throwable throwable) {
        log.error("机器人服务调用失败:{}", throwable.getMessage());

        return new RemoteBotJobService() {
            @Override
            public R polling() {
                return R.fail("轮询日志结果失败:" + throwable.getMessage());
            }

            @Override
            public R feedback() {
                return R.fail("反馈求文情况失败:" + throwable.getMessage());
            }

            @Override
            public R day() {
                return R.fail("每天总结失败:" + throwable.getMessage());
            }

            @Override
            public R week() {
                return R.fail("每周总结失败:" + throwable.getMessage());
            }

            @Override
            public R month() {
                return R.fail("每月总结失败:" + throwable.getMessage());
            }
        };
    }
}
