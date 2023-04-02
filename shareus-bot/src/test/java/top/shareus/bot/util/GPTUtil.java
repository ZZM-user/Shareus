package top.shareus.bot.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.HashMap;

/**
 * @author 17602
 */
public class GPTUtil {
    private static final String TOKEN = "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";
    private static HashMap<String, Object> map = new HashMap() {{
        put("model", "gpt-3.5-turbo");
        put("messages", Lists.newArrayList());
    }};
    
    public static void main(String[] args) {
        String talk = GPTUtil.talk("百度", "你好");
        System.out.println("talk = " + talk);
    }
    
    public static String talk(String sender, String message) {
        HashMap<String, Object> msg = new HashMap() {{
            put("role", "user");
            put("content", message);
        }};
        
        map.put("messages", ListUtil.of(msg));
        
        HttpResponse execute = null;
        try {
            execute = HttpRequest.post("https://api.openai.com/v1/chat/completions")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + TOKEN)
                    .body(JSONUtil.toJsonPrettyStr(map))
                    .timeout(50000)
                    .execute();
        } catch (Exception e) {
            return " 我被玩坏了,先别找我了";
        }
        
        String body = execute.body();
        return " 我被玩坏了,先别找我了";
    }
}
