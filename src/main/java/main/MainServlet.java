package main;

import command.*;
import data.ConfigSystem;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.APIRateLimit;
import tool.APISurvivePool;
import tool.VariablePool;
import util.FriendMessage;
import util.GroupMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static tool.ObjectArrayCaster.toLong;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MainServlet.class);
    private static Map<Pattern, BaseGroupMessageCommand> apiList = new LinkedHashMap<>();
    public static final long[] followGroup = toLong(ConfigSystem.getInstance().getConfigArray("Follow_Group_Uid"));
    private static final long[] blackListPeople = toLong(ConfigSystem.getInstance().getConfigArray("BlackList_Uid"));
    private static final long[] recordGroup = toLong(ConfigSystem.getInstance().getConfigArray("Record_Group_Uid"));

    public static Map<Pattern, BaseGroupMessageCommand> getApiList() {
        return apiList;
    }

    private static APIRateLimit cooling = new APIRateLimit(4000L);

    MainServlet() {
        // CUSTOM 注意：此处configure的顺序决定优先级。
        // 开发者非常不建议修改此处内容，容易造成奇怪的问题。
        MainServlet.configure(TestGroup.getInstance().getKeyWordRegex(), TestGroup.getInstance());
        MainServlet.configure(GCommandManager.getInstance().getKeyWordRegex(), GCommandManager.getInstance());
        MainServlet.configure(GBlacklist.getInstance().getKeyWordRegex(), GBlacklist.getInstance());
        MainServlet.configure(GHelp.getInstance().getKeyWordRegex(), GHelp.getInstance());
        MainServlet.configure(GVersion.getInstance().getKeyWordRegex(), GVersion.getInstance());
        MainServlet.configure(GMo.getInstance().getKeyWordRegex(), GMo.getInstance());
        MainServlet.configure(GEcho.getInstance().getKeyWordRegex(), GEcho.getInstance());
        MainServlet.configure(GXiaoIce.getInstance().getKeyWordRegex(), GXiaoIce.getInstance());
    }


    public static BaseGroupMessageCommand getAPIByKeyword(String keyword) {
        for (Map.Entry<Pattern, BaseGroupMessageCommand> patternAPIEntry : apiList.entrySet()) {
            Pattern key = patternAPIEntry.getKey();
            BaseGroupMessageCommand value = patternAPIEntry.getValue();
            if (key.matcher(keyword).find())
                return value;
        }
        return null;
    }

    private static void configure(Pattern regex, BaseGroupMessageCommand api) {
        apiList.put(regex, api);
        APISurvivePool.getInstance().addAPI(api);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        JSONObject object = (JSONObject) new JSONTokener(req.getReader()).nextValue();
        if ((boolean) ConfigSystem.getInstance().getConfig("Debug")) logger.info(object.toString());
        if (object.isNull("post_type") || object.isNull("type")) return;
        if (!"receive_message".equals(object.getString("post_type")))
            return;
        //
        long timeLong = object.getLong("time");
        int Id = object.getInt("id");
        long senderUid = object.getLong("sender_uid");
        String sender = object.get("sender").toString();
        String content = object.get("content").toString();
        String type = object.getString("type");
        for (long thisBlackPeople : blackListPeople)
            if (thisBlackPeople == senderUid) return;
        if ("friend_message".equals(type)) {
            Recorder.getInstance().recodeFriendMessage(new FriendMessage(Id, timeLong, senderUid, sender, content));
            return;
        }
        long groupUid = object.getLong("group_uid");
        String group = object.get("group").toString();
        GroupMessage message = new GroupMessage(Id, timeLong, senderUid, sender, groupUid, group, content);
        for (long thisRecordGroup : recordGroup) {
            if (thisRecordGroup == groupUid)
                if ("group_message".equals(type))
                    Recorder.getInstance().recodeGroupMessage(message);
        }
        for (long thisFollowGroup : followGroup)
            if (groupUid == thisFollowGroup) {
                for (Map.Entry<Pattern, BaseGroupMessageCommand> patternAPIEntry : apiList.entrySet()) {
                    BaseGroupMessageCommand value = patternAPIEntry.getValue();
                    if (doCheck(patternAPIEntry.getKey(), value, message))
                        value.doPost(message);
                }
            }
    }

    private boolean doCheck(Pattern key, BaseGroupMessageCommand value, GroupMessage groupMessage) {
        String lowerContent = groupMessage.getContent().toLowerCase();
        long time = groupMessage.getTimeLong();
        String sender = groupMessage.getSenderNickName();
        if (!key.matcher(lowerContent).find()) return false;
        if (!cooling.trySet(time)) {
            if (!VariablePool.Limit_Noticed) {
                groupMessage.response("@\u2005" + sender +
                        " 对不起，您的指令超频。4s内仅能有一次指令输入，未到4s内的输入将被忽略。注意：此消息仅会显示一次。");
                VariablePool.Limit_Noticed = true;
            }
            return false;
        }
        if (!APISurvivePool.getInstance().isSurvive(value)) {
            if (!APISurvivePool.getInstance().isNoticed(value)) {
                groupMessage.response("@\u2005" + sender + " 对不起，您调用的指令响应器目前已被停止；" +
                        "注意：此消息仅会显示一次。");
                APISurvivePool.getInstance().setNoticed(value);
            }
            return false;
        } else {
            try {
                if (!lowerContent.equals(new String(lowerContent
                        .getBytes("GB2312"), "GB2312"))) {
                    groupMessage.response("@\u2005" + sender + " 您的指示编码好像不对劲啊(╯︵╰,)");
                    return false;
                }
            } catch (UnsupportedEncodingException ignore) {
            }
            return true;
        }
    }
}
