package api;

import org.json.JSONObject;
import tool.Response;
import tool.VariablePool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Mo implements API {
    private static Mo instance = null;

    static List<String> keywords = new ArrayList<>();

    static {
        // CUSTOM 以下配置激活语句，即用户消息中出现这些关键词激活膜蛤功能
        keywords.add("+1s");
        keywords.add("膜蛤");
        keywords.add("苟");
        keywords.add("+1s");
        keywords.add("-1s");
        keywords.add("续命");
        keywords.add("州长夫人");
    }

    static Mo getInstance() {
        if (instance == null) instance = new Mo();
        return instance;
    }

    @Override
    public void doPost(JSONObject object) {
        String group_uid = object.get("group_uid").toString();
        if (VariablePool.Mo_Reach_Max) return;
        if (VariablePool.Mo_Count >= 50) {
            Response.responseGroup(group_uid, "哼！你们今天膜的太多啦！长者肯定会生气的！");
            VariablePool.Mo_Reach_Max = true;
            return;
        }
        // CUSTOM 以下配置膜蛤语句。本API被激活时语句将随机显示。
        String[] responseMessages = {"我Avalon都迫不得已给长者+1s啊... ...",
                "哈哈蛤哈",
                "你们有没有... ...就是那种... ...那种... ...诗？",
                "那首诗怎么念来着？苟利国家... ...",
                "要不我Avalon给大家发一个念诗红包吧？",
                "我觉得我Avalon需要给长者续什么才行啊",
                "我跟你江，你们这是要负泽任的，民白不？",
                "还是去弹夏威夷吉他吧！",
                "枸杞有养生功效，古人云：枸利果佳生食宜，气阴火服必驱之",
                "下面我们有请州长夫人演唱！"};
        Response.responseGroup(group_uid, responseMessages[new Random().nextInt(responseMessages.length)]);
        VariablePool.Mo_Count++;
    }
}
