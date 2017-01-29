package api;

import org.json.JSONObject;
import tool.Response;
import tool.VariablePool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Mo implements API {
    private static Mo instance = null;

    static List<String> keywords = new ArrayList<>();

    static {
        keywords.add("+1s");
        keywords.add("膜蛤");
        keywords.add("苟");
        keywords.add("+1s");
        keywords.add("-1s");
        keywords.add("续命");
        keywords.add("州长夫人");
    }

    public static Mo getInstance() {
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
        response(group_uid);
        VariablePool.Mo_Count++;
    }

    @Override
    public void response(String groupNumber) {
        String[] responseMessages = {"我Avalon都迫不得已给长者+1s啊... ...",
                "蛤哈哈哈",
                "你们有没有... ...就是那种... ...那种... ...诗？",
                "那首诗怎么念来着？苟利国家... ...",
                "要不我Avalon给大家发一个念诗红包吧？",
                "我觉得我Avalon需要给长者续什么才行啊",
                "你们这是要负泽任的，民白不？",
                "还是去弹夏威夷吉他吧！"};
        Response.responseGroup(groupNumber, responseMessages[new Random().nextInt(responseMessages.length)]);
    }
}
