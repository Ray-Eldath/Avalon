package main;

import api.*;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.APIRateLimit;
import tool.APISurvivePool;
import tool.Response;
import tool.VariablePool;
import util.FriendMessage;
import util.GroupMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MainServlet.class);
    private static Map<Pattern, GroupMessageAPI> apiList = new LinkedHashMap<>();
    public static final long[] followGroup = {617118724};
    public static final long[] followPeople = {951394653, 360736041, 1464443139, 704639565};
    private static final long[] blackListPeople = {2980403073L};

    public static Map<Pattern, GroupMessageAPI> getApiList() {
        return apiList;
    }

    // CUSTOM 指令最小间隔，几秒才能发出一次指令（单位：毫秒），注意同步修改下文注释处。
    private static APIRateLimit cooling = new APIRateLimit(4000L);

    MainServlet() {
        // CUSTOM 注意：此处configure的顺序决定优先级。
        // 开发者非常不建议修改此处内容，容易造成奇怪的问题。
        MainServlet.configure(TestGroup.getInstance().getKeyWordRegex(), TestGroup.getInstance());
        MainServlet.configure(APIManager.getInstance().getKeyWordRegex(), APIManager.getInstance());
        MainServlet.configure(Blacklist.getInstance().getKeyWordRegex(), Blacklist.getInstance());
        MainServlet.configure(Help.getInstance().getKeyWordRegex(), Help.getInstance());
        MainServlet.configure(Version.getInstance().getKeyWordRegex(), Version.getInstance());
        MainServlet.configure(Mo.getInstance().getKeyWordRegex(), Mo.getInstance());
        MainServlet.configure(Echo.getInstance().getKeyWordRegex(), Echo.getInstance());
        MainServlet.configure(XiaoIce.getInstance().getKeyWordRegex(), XiaoIce.getInstance());
    }


    public static GroupMessageAPI getAPIByKeyword(String keyword) {
        for (Map.Entry<Pattern, GroupMessageAPI> patternAPIEntry : apiList.entrySet()) {
            Pattern key = patternAPIEntry.getKey();
            GroupMessageAPI value = patternAPIEntry.getValue();
            if (key.matcher(keyword).find())
                return value;
        }
        return null;
    }

    private static void configure(Pattern regex, GroupMessageAPI api) {
        apiList.put(regex, api);
        APISurvivePool.getInstance().addAPI(api);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        JSONObject object = (JSONObject) new JSONTokener(req.getReader()).nextValue();
        // logger.info(object.toString());
        if (object.isNull("post_type") || object.isNull("type")) return;
        if (!"receive_message".equals(object.getString("post_type")))
            return;
        //
        long timeLong = object.getLong("time");
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeLong), ZoneId.of("Asia/Shanghai"));
        int Id = object.getInt("id");
        long senderUid = object.getLong("sender_uid");
        String sender = object.get("sender").toString();
        String content = object.get("content").toString();
        String type = object.getString("type");
        String lowerContent = content.toLowerCase();
        //
        for (long thisBlackPeople : blackListPeople)
            if (thisBlackPeople == senderUid) return;
        if ("friend_message".equals(type)) {
            Recorder.getInstance().recodeFriendMessage(new FriendMessage(Id, time, senderUid, sender, content));
            return;
        }
        long groupUid = object.getLong("group_uid");
        String group = object.get("group").toString();
        GroupMessage message = new GroupMessage(Id, time, senderUid, sender, groupUid, group, content);
        if ("group_message".equals(type))
            Recorder.getInstance().recodeGroupMessage(message);
        else return;
        //
        for (long thisFollowGroup : followGroup)
            if (groupUid == thisFollowGroup) {
                for (Map.Entry<Pattern, GroupMessageAPI> patternAPIEntry : apiList.entrySet()) {
                    GroupMessageAPI value = patternAPIEntry.getValue();
                    if (doCheck(patternAPIEntry.getKey(), value, lowerContent, groupUid, sender, timeLong))
                        value.doPost(message);
                }
            }
    }

    private boolean doCheck(Pattern key, GroupMessageAPI value, String lowerContent, long groupUid, String sender,
                            long time) {
        if (!key.matcher(lowerContent).find()) return false;
        if (!cooling.trySet(time)) {
            if (!VariablePool.Limit_Noticed) {
                // CUSTOM 若修改了指令最小间隔，请同步修改此处。
                Response.responseGroup(groupUid, "@" + sender +
                        " 对不起，您的指令超频。4s内仅能有一次指令输入，未到4s内的输入将被忽略。" +
                        "注意：此消息仅会显示一次。");
                //
                VariablePool.Limit_Noticed = true;
            }
            return false;
        }
        if (!APISurvivePool.getInstance().isSurvive(value)) {
            if (!APISurvivePool.getInstance().isNoticed(value)) {
                Response.responseGroup(groupUid, "@" + sender +
                        " 对不起，您调用的方法目前已被停止；注意：此消息仅会显示一次。");
                APISurvivePool.getInstance().setNoticed(value);
            }
            return false;
        } else {
            try {
                if (!lowerContent.equals(new String(lowerContent
                        .getBytes("GB2312"), "GB2312"))) {
                    Response.responseGroup(groupUid, "@" + sender + " 您的指示编码好像不对劲啊(╯︵╰,)");
                    return false;
                }
            } catch (UnsupportedEncodingException ignore) {
            }
            return true;
        }
    }
}
