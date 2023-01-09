package top.shareus.mirai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.shareus.common.security.annotation.EnableCustomConfig;
import top.shareus.common.security.annotation.EnableRyFeignClients;
import top.shareus.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 系统模块
 *
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class ShareusMiraiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShareusMiraiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  mirai模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
