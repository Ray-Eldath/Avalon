package main;

import command.*;
import data.ConfigSystem;
import extend.*;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.APIRateLimit;
import tool.APISurvivePool;
import tool.ConstantPool;
import tool.VariablePool;
import util.FriendMessage;
import util.GroupMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static tool.ObjectCaster.toLongArray;
import static tool.ObjectCaster.toStringArray;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MainServlet.class);
    private static final Map<Pattern, BaseGroupMessageCommandRunner> apiList = new LinkedHashMap<>();
    private static final Map<Pattern, BaseGameCommandRunner> gameApiList = new LinkedHashMap<>();
    private static long[] adminUid = toLongArray(ConfigSystem
            .getInstance().getConfigArray("Admin_Uid"));
    private static long[] followGroup = toLongArray(ConfigSystem
            .getInstance().getConfigArray("Follow_Group_Uid"));
    private static long[] gameModeAllowedGroup = toLongArray(ConfigSystem
            .getInstance().getConfigArray("Game_Mode_Enabled_Group_Uid"));
    private static long[] recordGroup = toLongArray(ConfigSystem
            .getInstance().getConfigArray("Record_Group_Uid"));
    private static long[] blackListPeople = toLongArray(ConfigSystem
            .getInstance().getConfigArray("BlackList_Uid"));
    private static Map<Long, Integer> blackListPeopleMap = new HashMap<>();
    private static String[] blockList = toStringArray(ConfigSystem
            .getInstance().getConfigArray("Block_Words"));
    public static final int punishFrequency = (int) ConfigSystem.getInstance()
            .getConfig("Block_Words_Punish_Frequency");
    private static final APIRateLimit cooling = new APIRateLimit(3000L);

    public static Map<Pattern, BaseGroupMessageCommandRunner> getApiList() {
        return apiList;
    }

    static {
        gameApiList.put(GRegister.getInstance().getKeyWordRegex(), GRegister.getInstance());
        gameApiList.put(GNewGroup.getInstance().getKeyWordRegex(), GNewGroup.getInstance());
        gameApiList.put(GJoinGroup.getInstance().getKeyWordRegex(), GJoinGroup.getInstance());
        gameApiList.put(GGo.getInstance().getKeyWordRegex(), GGo.getInstance());
        //
        MainServlet.configure(TestGroup.getInstance().getKeyWordRegex(), TestGroup.getInstance());
        MainServlet.configure(GGameInfo.getInstance().getKeyWordRegex(), GGameInfo.getInstance());
        MainServlet.configure(GGameLoad.getInstance().getKeyWordRegex(), GGameLoad.getInstance());
        MainServlet.configure(GCommandManager.getInstance().getKeyWordRegex(), GCommandManager.getInstance());
        MainServlet.configure(GShutdown.getInstance().getKeyWordRegex(), GShutdown.getInstance());
        MainServlet.configure(GBlacklist.getInstance().getKeyWordRegex(), GBlacklist.getInstance());
        MainServlet.configure(GFlush.getInstance().getKeyWordRegex(), GFlush.getInstance());
        MainServlet.configure(GHelp.getInstance().getKeyWordRegex(), GHelp.getInstance());
        MainServlet.configure(GVersion.getInstance().getKeyWordRegex(), GVersion.getInstance());
        MainServlet.configure(GMo.getInstance().getKeyWordRegex(), GMo.getInstance());
        MainServlet.configure(GEcho.getInstance().getKeyWordRegex(), GEcho.getInstance());
        MainServlet.configure(GXiaoIce.getInstance().getKeyWordRegex(), GXiaoIce.getInstance());
    }

    MainServlet() {
        for (long thisBlackPeople : blackListPeople)
            blackListPeopleMap.put(thisBlackPeople, punishFrequency + 1);
    }

    public static BaseGroupMessageCommandRunner getAPIByKeyword(String keyword) {
        for (Map.Entry<Pattern, BaseGroupMessageCommandRunner> patternAPIEntry : apiList.entrySet()) {
            Pattern key = patternAPIEntry.getKey();
            BaseGroupMessageCommandRunner value = patternAPIEntry.getValue();
            if (key.matcher(keyword).find())
                return value;
        }
        return null;
    }

    private static void configure(Pattern regex, BaseGroupMessageCommandRunner api) {
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
        String lowerContent = content.toLowerCase();
        String type = object.getString("type");
        for (long thisBlackPeople : blackListPeople)
            if (thisBlackPeople == senderUid) return;
        if ("friend_message".equals(type)) {
            Recorder.getInstance().recodeFriendMessage(new FriendMessage(Id, timeLong, senderUid, sender, content));
            return;
        }
        if (!("friend_message".equals(type) || "group_message".equals(type)))
            return;
        long groupUid = object.getLong("group_uid");
        String group = object.get("group").toString();
        GroupMessage message = new GroupMessage(Id, timeLong, senderUid, sender, groupUid, group, content);
        boolean admin = false;
        for (long thisFollowGroup : followGroup) {
            if (groupUid == thisFollowGroup) {
                if (!preCheck(lowerContent)) return;
                if (!checkEncode(lowerContent, message)) return;
                for (long thisAdmin : adminUid)
                    if (thisAdmin == senderUid) {
                        admin = true;
                        break;
                    } else admin = false;
                if (!admin) {
                    if (ConstantPool.Setting.Block_Words_Punishment_Mode_Enabled) {
                        if (blackListPeopleMap.containsKey(senderUid)) {
                            if (blackListPeopleMap.get(senderUid) >= punishFrequency) {
                                message.response("@" + sender +
                                        " 您的帐号由于发送过多不允许关键字，现已被屏蔽~o(╯□╰)o！");
                                return;
                            }
                        } else blackListPeopleMap.put(senderUid, 0);
                    }
                }
                for (String thisBlockString : blockList)
                    if (content.replace(" ", "").contains(thisBlockString)) {
                        String notice = "您发送的消息含有不允许的关键词！";
                        if (ConstantPool.Setting.Block_Words_Punishment_Mode_Enabled) {
                            notice = "您发送的消息含有不允许的关键词，注意：" + punishFrequency +
                                    "次发送不允许关键词后帐号将被屏蔽！⊙﹏⊙!";
                            blackListPlus(senderUid);
                        }
                        message.response("@" + sender + " " + notice);
                        return;
                    }
                for (Map.Entry<Pattern, BaseGroupMessageCommandRunner> patternAPIEntry : apiList.entrySet()) {
                    BaseGroupMessageCommandRunner value = patternAPIEntry.getValue();
                    if (doCheck(patternAPIEntry.getKey(), value, message)) {
                        value.doPost(message);
                        return;
                    }
                }
                if (ConstantPool.GameMode.IsEnabled)
                    for (long thisGameModeGroup : gameModeAllowedGroup)
                        if (groupUid == thisGameModeGroup)
                            for (Map.Entry<Pattern, BaseGameCommandRunner> gameApiListEntry : gameApiList.entrySet())
                                if (gameApiListEntry.getKey().matcher(content).find()) {
                                    gameApiListEntry.getValue().doPost(message);
                                    return;
                                }
            }
        }
        for (long thisRecordGroup : recordGroup)
            if (thisRecordGroup == groupUid) {
                Recorder.getInstance().recodeGroupMessage(message);
                return;
            }
    }

    private boolean checkEncode(String content, GroupMessage message) {
        try {
            if (!content.equals(new String(content.getBytes("GB2312"), "GB2312"))) {
                message.response("@" + message.getSenderNickName() + " 您的指示编码好像不对劲啊╮(╯_╰)╭");
                return false;
            }
        } catch (UnsupportedEncodingException ignore) {
        }
        return true;
    }

    private boolean doCheck(Pattern key, BaseGroupMessageCommandRunner value, GroupMessage groupMessage) {
        String lowerContent = groupMessage.getContent().toLowerCase();
        long time = groupMessage.getTimeLong();
        String sender = groupMessage.getSenderNickName();
        if (!key.matcher(lowerContent).find()) return false;
        if (!cooling.trySet(time)) {
            if (!VariablePool.Limit_Noticed) {
                groupMessage.response("@" + sender +
                        " 对不起，您的指令超频。3s内仅能有一次指令输入，未到3s内的输入将被忽略。注意：此消息仅会显示一次。");
                VariablePool.Limit_Noticed = true;
            }
            return false;
        }
        if (!APISurvivePool.getInstance().isSurvive(value)) {
            if (!APISurvivePool.getInstance().isNoticed(value)) {
                groupMessage.response("@" + sender + " 对不起，您调用的指令响应器目前已被停止；" +
                        "注意：此消息仅会显示一次。");
                APISurvivePool.getInstance().setNoticed(value);
            }
            return false;
        }
        return true;
    }

    public static long[] getAdminUid() {
        return adminUid;
    }

    public static long[] getFollowGroup() {
        return followGroup;
    }

    public static Map<Long, Integer> getSetBlackListPeopleMap() {
        return blackListPeopleMap;
    }

    private boolean preCheck(String lowerContent) {
        boolean match = true;
        //
        for (Pattern thisPattern : gameApiList.keySet()) {
            if (thisPattern.matcher(lowerContent).find()) {
                return true;
            } else match = false;
        }
        //
        for (Pattern thisPattern : apiList.keySet()) {
            if (thisPattern.matcher(lowerContent).find()) {
                return true;
            } else match = false;
        }
        return match;
    }

    private void blackListPlus(long senderUid) {
        int pastValue;
        pastValue = blackListPeopleMap.get(senderUid);
        blackListPeopleMap.put(senderUid, ++pastValue);
    }
}
