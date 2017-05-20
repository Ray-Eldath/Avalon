package avalon.group;

import avalon.extend.Recorder;
import avalon.main.MessageChecker;
import avalon.tool.APIRateLimit;
import avalon.tool.ConfigSystem;
import avalon.tool.RunningData;
import avalon.tool.pool.APISurvivePool;
import avalon.tool.pool.ConstantPool;
import avalon.tool.pool.VariablePool;
import avalon.util.GroupMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import static avalon.main.RegisterResponder.register;
import static avalon.tool.ObjectCaster.toLongArray;
import static avalon.tool.ObjectCaster.toStringArray;

/**
 * Created by Eldath Ray on 2017/3/30.
 *
 * @author Eldath Ray
 */
public class MainGroupMessageHandler {
    private static final Map<Pattern, BaseGroupMessageResponder> apiList = new LinkedHashMap<>();

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
    private static final int punishFrequency = (int) ConfigSystem.getInstance()
            .getConfig("Block_Words_Punish_Frequency");
    private static final APIRateLimit cooling = new APIRateLimit(3000L);

    private static MainGroupMessageHandler instance = new MainGroupMessageHandler();
    private static final Logger logger = LoggerFactory.getLogger(MainGroupMessageHandler.class);

    Map<Pattern, BaseGroupMessageResponder> getApiList() {
        return apiList;
    }

    public static MainGroupMessageHandler getInstance() {
        return instance;
    }

    private MainGroupMessageHandler() {
        for (long thisBlackPeople : blackListPeople)
            blackListPeopleMap.put(thisBlackPeople, punishFrequency + 1);
        int length = followGroup.length;
        long[] data = new long[length + 1];
        System.arraycopy(followGroup, 0, data, 1, length);
        data[0] = 100000;
        followGroup = data;
    }

    static {
        /*
         * 指令优先级排序依据：单词 >> 多词，管理类 >> 服务类 >> 娱乐类，触发类 >> 自由类
         */
        // 特殊优先
        register(Test.getInstance());
        // 管理类
        register(Shutdown.getInstance());
        register(Flush.getInstance());
        register(Manager.getInstance());
        register(Blacklist.getInstance());
        // 服务类
        register(Help.getInstance());
        register(Version.getInstance());
        register(Echo.getInstance());
        // 娱乐类
        register(Mo.getInstance());
        register(AnswerMe.getInstance());
    }

    static BaseGroupMessageResponder getGroupResponderByKeyword(String keyword) {
        for (Map.Entry<Pattern, BaseGroupMessageResponder> patternAPIEntry : apiList.entrySet()) {
            Pattern key = patternAPIEntry.getKey();
            BaseGroupMessageResponder value = patternAPIEntry.getValue();
            if (key.matcher(keyword).find())
                return value;
        }
        return null;
    }

    public void handle(GroupMessage message) {
        if (!ConstantPool.Basic.Debug)
            Recorder.getInstance().recodeGroupMessage(message);
        //FIXME MessageHooker.handle(message);
        long groupUid = message.getGroupUid();
        String content = message.getContent();
        String plainContent = content.trim().toLowerCase().replaceAll("[\\pP\\p{Punct}]", "");
        String sender = message.getSenderNickName();
        long senderUid = message.getSenderUid();
        LongStream adminUidStream = Arrays.stream(adminUid);
        LongStream followGroupStream = Arrays.stream(followGroup);
        boolean admin = adminUidStream.anyMatch(e -> e == senderUid);
        if (followGroupStream.anyMatch(e -> e == groupUid)) {
            if (ConstantPool.Setting.Block_Words_Punishment_Mode_Enabled)
                if (blackListPeopleMap.containsKey(senderUid)) {
                    if (blackListPeopleMap.get(senderUid) >= punishFrequency) {
                        logger.info("Account " + senderUid + ":" + sender + " was blocked. Please entered " +
                                "\"avalon blacklist remove " + senderUid + "\" to the group " + groupUid + ":" +
                                message.getGroupName() + " if you really want to unblock this account.");
                        if (!admin)
                            message.response("@" + sender +
                                    " 您的帐号由于发送过多不允许关键字，现已被屏蔽~o(╯□╰)o！");
                        return;
                    }
                } else blackListPeopleMap.put(senderUid, 0);
            for (String thisBlockString : blockList)
                if (plainContent.contains(thisBlockString)) {
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
                    if (MessageChecker.checkEncode(message))
                        value.doPost(message);
                    return;
                }
            }
        }
        if (Arrays.stream(recordGroup).anyMatch(e -> e == groupUid))
            Recorder.getInstance().recodeGroupMessage(message);
    }

    private boolean doCheck(Pattern key, BaseGroupMessageResponder value, GroupMessage groupMessage) {
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

    public static void main(String[] args) {
        ConfigSystem.getInstance();
        RunningData.getInstance();
        new ConstantPool.Basic();
        new ConstantPool.Address();
        if (!ConstantPool.Basic.Debug)
            return;
        Scanner scanner = new Scanner(System.in);
        int id = 0;
        while (true) {
            System.out.print("Input here:");
            String content = scanner.nextLine();
            GroupMessage message = new GroupMessage(++id, LocalDateTime.now(),
                    10000, "Test", 100000, "Test Group", content);
            MainGroupMessageHandler.getInstance().handle(message);
            System.out.println("===");
        }
    }

    private void blackListPlus(long senderUid) {
        int pastValue = blackListPeopleMap.get(senderUid);
        blackListPeopleMap.put(senderUid, ++pastValue);
    }

    public static void addGroupMessageResponder(BaseGroupMessageResponder responder) {
        apiList.put(responder.getKeyWordRegex(), responder);
    }

    static long[] getAdminUid() {
        return adminUid;
        //FIXME 是否需要adminUid.clone()以防止数据修改？
    }

    public long[] getFollowGroup() {
        return followGroup;
    }

    static int getPunishFrequency() {
        return punishFrequency;
    }

    static Map<Long, Integer> getSetBlackListPeopleMap() {
        return blackListPeopleMap;
    }
}
