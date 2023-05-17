package top.shareus.bot.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.shareus.common.security.annotation.EnableCustomConfig;
import top.shareus.common.security.annotation.EnableRyFeignClients;
import top.shareus.common.swagger.annotation.EnableCustomSwagger2;

@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class ShareusBotWebApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ShareusBotWebApplication.class, args);
		System.out.println("//       程序出Bug了？          \n" +
								   "//      　　　∩∩               \n" +
								   "//      　　（´･ω･）            \n" +
								   "//      　 ＿|　⊃／(＿＿_       \n" +
								   "//      　／ └-(＿＿＿／        \n" +
								   "//      　￣￣￣￣￣￣￣         \n" +
								   "//      算了反正不是我写的       \n" +
								   "//      　　 ⊂⌒／ヽ-、＿         \n" +
								   "//      　／⊂_/＿＿＿＿ ／       \n" +
								   "//      　￣￣￣￣￣￣￣         \n" +
								   "//      万一是我写的呢           \n" +
								   "//      　　　∩∩                \n" +
								   "//      　　（´･ω･）            \n" +
								   "//      　 ＿|　⊃／(＿＿_       \n" +
								   "//      　／ └-(＿＿＿／        \n" +
								   "//      　￣￣￣￣￣￣￣         \n" +
								   "//      算了反正改了一个又出三个  \n" +
								   "//      　　 ⊂⌒／ヽ-、＿        \n" +
								   "//      　／⊂_/＿＿＿＿ ／      \n" +
								   "//      　￣￣￣￣￣￣￣        ");
	}
	
}
