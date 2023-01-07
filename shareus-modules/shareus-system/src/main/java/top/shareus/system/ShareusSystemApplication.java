package top.shareus.system;

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
public class ShareusSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShareusSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
