package group;

import extend.BaseGameResponder;
import extend.Recorder;
import main.MainServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.APISurvivePool;
import tool.ConstantPool;
import tool.VariablePool;
import util.GroupMessage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static main.MainServlet.*;

/**
 * Created by Eldath Ray on 2017/3/30.
 *
 * @author Eldath Ray
 */
public class MainGroupMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MainServlet.class);
    private static final Map<Pattern, BaseGroupMessageResponder> apiList = new LinkedHashMap<>();
    private static final Map<Pattern, BaseGameResponder> gameApiList = new LinkedHashMap<>();

    public static void handle(GroupMessage message) {
        Recorder.getInstance().recodeGroupMessage(message);
        long groupUid = message.getGroupUid();
        String content = message.getContent();
        String lowerContent = content.toLowerCase();
        String sender = message.getSenderNickName();
        long senderUid = message.getSenderUid();
        boolean admin = false;
        for (long thisFollowGroup : followGroup) {
            if (groupUid == thisFollowGroup) {
                if (!preCheck(lowerContent)) return;
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
                for (Map.Entry<Pattern, BaseGroupMessageResponder> patternAPIEntry : apiList.entrySet()) {
                    BaseGroupMessageResponder value = patternAPIEntry.getValue();
                    if (doCheck(patternAPIEntry.getKey(), value, message)) {
                        value.doPost(message);
                        return;
                    }
                }
                if (ConstantPool.GameMode.IsEnabled)
                    for (long thisGameModeGroup : gameModeAllowedGroup)
                        if (groupUid == thisGameModeGroup)
                            for (Map.Entry<Pattern, BaseGameResponder> gameApiListEntry : gameApiList.entrySet())
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

    private static boolean doCheck(Pattern key, BaseGroupMessageResponder value, GroupMessage groupMessage) {
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

    private static boolean preCheck(String lowerContent) {
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

    private static void blackListPlus(long senderUid) {
        int pastValue;
        pastValue = blackListPeopleMap.get(senderUid);
        blackListPeopleMap.put(senderUid, ++pastValue);
    }
}
