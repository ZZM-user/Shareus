package top.shareus.gen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.shareus.common.security.annotation.EnableCustomConfig;
import top.shareus.common.security.annotation.EnableRyFeignClients;
import top.shareus.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 代码生成
 *
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class ShareusGenApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShareusGenApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  代码生成模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
