package top.shareus.bot.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.shareus.ShareusBotApplication;


@SpringBootTest(classes = ShareusBotApplication.class)
class AlistUtilsTest {

    @Test
    void getAuthorization() {
        String authorization = AlistUtils.getAuthorization();
        System.out.println("authorization = " + authorization);
    }
}