package api;

import org.json.JSONObject;
import tool.Response;
import tool.VariablePool;

import java.util.Random;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Mo implements API {
    static final String[] keyWords = {"mo", "膜蛤", "蛤？", "+1s", "-1s"};
    private static Mo instance = null;

    static API getInstance() {
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
                "你们这是要负泽任的，民白不？"};
        Response.responseGroup(groupNumber, responseMessages[new Random().nextInt(responseMessages.length)]);
    }
}
