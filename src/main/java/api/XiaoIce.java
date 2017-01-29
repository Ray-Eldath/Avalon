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
    //private static boolean first = true;

    static {
        keywords.add("Avalon tell me");
        keywords.add("Avalon answer me");
        keywords.add("阿瓦隆回答我");
        keywords.add("阿瓦隆告诉我");
        keywords.add("太短怎么才能变长");
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

        if (!content.contains(" ")) {
            Response.responseGroup(group_uid, "您的指示不合规范嘛(╯︵╰,)");
            return;
        }
        String text = content.split(" ")[1];
        if (strIsEnglish(text) && text.length() < 6) {
            Response.responseGroup(group_uid, "@" + sender + " 您的消息过短~o(╯□╰)o！");
            return;
        } else if (text.length() < 4) {
            Response.responseGroup(group_uid, "@" + sender + " 您的消息过短~o(╯□╰)o！");
            return;
        }
        if (content.charAt(1) >= 'A' && content.charAt(1) <= 'Z') {
            if (content.length() < 22) {
                Response.responseGroup(group_uid, "@" + sender + " 您的消息过短~o(╯□╰)o！");
                return;
            }
        } else {
            if (content.length() < 12) {
                Response.responseGroup(group_uid, "@" + sender + " 您的消息过短~o(╯□╰)o！");
                return;
            }
        }
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

    private boolean strIsEnglish(String word) {
        boolean sign = true;
        for (int i = 0; i < word.length(); i++)
            if (!(word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')
                    && !(word.charAt(i) >= 'a' && word.charAt(i) <= 'z'))
                return false;
        return true;
    }
}
