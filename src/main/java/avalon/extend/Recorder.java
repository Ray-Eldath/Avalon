package avalon.extend;

import avalon.api.util.FriendMessage;
import avalon.api.util.GroupMessage;
import avalon.tool.ConstantPool;
import avalon.tool.RunningData;
import data.ConfigSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath on 2017/2/3 0003.
 *
 * @author Eldath
 */
public class Recorder {
    private static final List<FriendMessage> friendMessageRecord = new ArrayList<>();
    private static final List<GroupMessage> groupMessageRecord = new ArrayList<>();
    private static Recorder instance = null;
    private static final int MAX_RECODE_LIST_SIZE = 10;
    private static final int MAX_RECORD_GROUP_MESSAGE_COUNT = (int) ConfigSystem.getInstance()
            .getConfig("Max_Recorded_Group_Message_Count");
    private static final int MAX_RECORD_FRIEND_MESSAGE_COUNT = (int) ConfigSystem.getInstance()
            .getConfig("Max_Recorded_Friend_Message_Count");
    private static int nowGroupCount = Integer.parseInt(RunningData.getInstance().get("group_message_recorded_count"));
    private static int nowFriendCount = Integer.parseInt(RunningData.getInstance().get("friend_message_recorded_count"));

    public static Recorder getInstance() {
        if (instance == null) instance = new Recorder();
        return instance;
    }

    public void recodeGroupMessage(GroupMessage message) {
        if (nowGroupCount > MAX_RECORD_GROUP_MESSAGE_COUNT) return;
        groupMessageRecord.add(message);
        nowGroupCount++;
        if (groupMessageRecord.size() > MAX_RECODE_LIST_SIZE)
            flushNow();
    }

    public void recodeFriendMessage(FriendMessage message) {
        if (nowFriendCount > MAX_RECORD_FRIEND_MESSAGE_COUNT) return;
        friendMessageRecord.add(message);
        nowFriendCount++;
        if (friendMessageRecord.size() > MAX_RECODE_LIST_SIZE)
            flushNow();
    }

    public void flushNow() {
        for (GroupMessage thisGroupMessage : groupMessageRecord)
            ConstantPool.Database.currentDatabaseOperator.add(thisGroupMessage);
        groupMessageRecord.clear();
        for (FriendMessage thisFriendMessage : friendMessageRecord)
            ConstantPool.Database.currentDatabaseOperator.add(thisFriendMessage);
        friendMessageRecord.clear();
    }

    public static int getNowGroupCount() {
        return nowGroupCount;
    }

    public static int getNowFriendCount() {
        return nowFriendCount;
    }

    public static int getMaxRecordGroupMessageCount() {
        return MAX_RECORD_GROUP_MESSAGE_COUNT;
    }

    public static int getMaxRecordFriendMessageCount() {
        return MAX_RECORD_FRIEND_MESSAGE_COUNT;
    }
}
