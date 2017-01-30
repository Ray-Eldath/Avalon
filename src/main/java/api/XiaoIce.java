package api;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.Response;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class XiaoIce implements API {
    private static final Logger logger = LoggerFactory.getLogger(XiaoIce.class);
    private static XiaoIce instance = null;
    static Map<String, Integer> blackList = new HashMap<>();
    static List<String> keywords = new ArrayList<>();
    private static List<String> blockList = new ArrayList<>();
    private static boolean first = true;

    static {
        // CUSTOM 以下配置激活语句，即用户消息中出现这些关键词才使用本API处理
        // 警告：必须前缀avalon/阿瓦隆并保持全小写，请在\api\Help中同步修改。
        // 开发者不建议修改此处内容。
        keywords.add("avalon tell me ");
        keywords.add("avalon answer me ");
        keywords.add("阿瓦隆回答我 ");
        keywords.add("阿瓦隆告诉我 ");
        // CUSTOM 以下配置不允许关键字
        blockList.add("寒寒");
        blockList.add("冰冰");
        blockList.add("冰封");
        blockList.add("小冰");
        blockList.add("小娜");
        blockList.add("微软");
        blockList.add("苹果");
        blockList.add("siri");
        blockList.add("apple");
        blockList.add("ice1000");
        blockList.add("eldath");
        blockList.add("女装");
        blockList.add("男装");
        blockList.add("妹妹");
        blockList.add("姐姐");
        blockList.add("蛤");
        blockList.add("膜");
        blockList.add("苟");
        blockList.add("太短");
        blockList.add("变长");
        // CUSTOM 以下配置黑名单。如不需要黑名单功能，请按下文所述注释掉代码。
        // 注意：黑名单功能包括“直接屏蔽某人”和“若某人触发关键词屏蔽多次则屏蔽某人”两部分。
    }

    static XiaoIce getInstance() {
        if (instance == null) instance = new XiaoIce();
        return instance;
    }

    @Override
    public void doPost(JSONObject object) {
        String group_uid = object.get("group_uid").toString();
        String sender = object.get("sender").toString();
        String sender_uid = object.get("sender_uid").toString();
        int pastValue;
        String content = object.get("content").toString()
                .trim()
                .toLowerCase()
                .replaceAll("[\\pP\\p{Punct}]", "");
        try {
            if (!content.contains(" ") ||
                    !content.equals(new String(content.getBytes("GB2312"), "GB2312"))) {
                Response.responseGroup(group_uid, "您的指示不合规范嘛(╯︵╰,)");
                return;
            }
        } catch (UnsupportedEncodingException ignore) {
        }
        String text = content;
        if ("".equals(text.replace(" ", ""))) {
            Response.responseGroup(group_uid, "@" + sender +
                    " 消息不能为空哦~(*∩_∩*)");
            return;
        }
        for (String thisKeyWord : keywords)
            text = text.replace(thisKeyWord, "");
        // CUSTOM 若不需要黑名单功能，请注释掉此处
        if (blackList.containsKey(sender_uid))
            if (blackList.get(sender_uid) > 2) {
                Response.responseGroup(group_uid, "@" + sender +
                        " 您的帐号由于发送过多不允许关键词，现已被屏蔽~o(╯□╰)o！");
                return;
            }
        //
        if (strIsEnglish(text)) {
            if (text.length() < 5) {
                Response.responseGroup(group_uid, "@" + sender + " 您的消息过短~o(╯□╰)o！");
                return;
            }
        } else if (text.length() < 3) {
            Response.responseGroup(group_uid, "@" + sender + " 您的消息过短~o(╯□╰)o！");
            return;
        }
        for (String thisBlockString : blockList)
            if (content.replace(" ", "").contains(thisBlockString)) {
                if (first) {
                    blackList.put(sender_uid, 0);
                    first = false;
                }
                // CUSTOM 若不需要黑名单功能，请注释掉此处并自行修改提示语句notice。
                String notice = "您发送的消息含有不允许的关键词，注意：3次发送不允许关键词后帐号将被屏蔽！⊙﹏⊙!";
                Response.responseGroup(group_uid, "@" + sender + " " + notice);
                pastValue = blackList.get(sender_uid);
                blackList.put(sender_uid, ++pastValue);
                //
                return;
            }
        for (String thisKeyWord : keywords)
            content = content.replace(thisKeyWord.toLowerCase(), "小冰");
        String responseXiaoIce = Response.responseXiaoIce(content);
        if (responseXiaoIce == null) return;
        Response.responseGroup(group_uid, "@" + sender + " " + responseXiaoIce);
    }

    private boolean strIsEnglish(String word) {
        for (int i = 0; i < word.length(); i++)
            if (!(word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')
                    && !(word.charAt(i) >= 'a' && word.charAt(i) <= 'z'))
                return false;
        return true;
    }
}
