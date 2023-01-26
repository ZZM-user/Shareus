package top.shareus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.shareus.common.security.annotation.EnableCustomConfig;
import top.shareus.common.security.annotation.EnableRyFeignClients;
import top.shareus.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 主应用程序
 *
 * @author zhaojl
 * @date 2023/01/22
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class ShareusBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShareusBotApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  bot模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }

}
