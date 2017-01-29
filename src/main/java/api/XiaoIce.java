package api;

import org.json.JSONObject;
import tool.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class XiaoIce implements API {
    private static XiaoIce instance = null;
    static List<String> keywords = new ArrayList<>();
    private static List<String> blockList = new ArrayList<>();
    private static boolean first = true;

    static {
        keywords.add("Avalon answer me");
        keywords.add("阿瓦隆回答我");
        //
        blockList.add("寒寒");
        blockList.add("冰冰");
        blockList.add("冰封");
        blockList.add("ice1000");
        blockList.add("eldath");
    }

    static XiaoIce getInstance() {
        if (instance == null) instance = new XiaoIce();
        return instance;
    }


    @Override
    public void doPost(JSONObject object) {
        String content = object.get("content").toString().toLowerCase();
        String group_uid = object.get("group_uid").toString();
        String sender = object.get("sender").toString();
        //long time = object.getLong("time");
        //long lastAllowTime = time + 2L;
        //if (first) {
        //    XiaoIceRateLimit.getInstance().setLastAllowTime(lastAllowTime);
        //    first = false;
        //}
        for (String thisBlockString : blockList)
            if (content.contains(thisBlockString)) {
                Response.responseGroup(group_uid, "@" + sender + " 您发送的消息含有不允许的关键词！⊙﹏⊙!");
                return;
            }
        //if (XiaoIceRateLimit.getInstance().isLimitNeeded(time)) {
        //    Response.responseGroup(group_uid, "@" + sender + " 指令超频！2秒内只能让阿瓦隆回答一次！(^ω^)");
        //XiaoIceRateLimit.getInstance().setLastAllowTime(lastAllowTime);
        //    return;
        //}
        for (String thisKeyWord : keywords)
            content = content.replace(thisKeyWord.toLowerCase(), "小冰");
        String XiaoIce = Response.responseXiaoIce(content);
        //XiaoIceRateLimit.getInstance().setLastAllowTime(lastAllowTime);
        if (XiaoIce == null) return;
        Response.responseGroup(group_uid, "@" + sender + " " + XiaoIce);
    }

    @Override
    public void response(String groupNumber) {
    }
}
