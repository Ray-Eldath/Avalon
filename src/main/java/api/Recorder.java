package api;

import org.eclipse.jetty.util.UrlEncoded;
import tool.ConstantPool;
import util.FriendMessage;
import util.GroupMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath on 2017/2/3 0003.
 *
 * @author Eldath
 */
public class Recorder {
    private static List<FriendMessage> friendMessageRecord = new ArrayList<>();
    private static List<GroupMessage> groupMessageRecord = new ArrayList<>();
    private static Recorder instance = null;
    private static final int MAX_RECODE_LIST_SIZE = 10;

    public static Recorder getInstance() {
        if (instance == null) instance = new Recorder();
        return instance;
    }

    public void recodeGroupMessage(GroupMessage message) {
        GroupMessage newMessage = new GroupMessage(message.getId(), message.getTime(), message.getSenderUid(),
                message.getSenderNickName(), message.getGroupUid(), message.getGroupName(),
                encodeString(message.getContent()));
        groupMessageRecord.add(newMessage);
        if (groupMessageRecord.size() > MAX_RECODE_LIST_SIZE)
            flushNow();
    }

    public void recodeFriendMessage(FriendMessage message) {
        FriendMessage newMessage = new FriendMessage(message.getId(), message.getTime(), message.getSenderUid(),
                message.getSenderNickName(), encodeString(message.getContent()));
        friendMessageRecord.add(newMessage);
        if (friendMessageRecord.size() > MAX_RECODE_LIST_SIZE)
            flushNow();
    }

    public void flushNow() {
        for (GroupMessage thisGroupMessage : groupMessageRecord)
            ConstantPool.Database.currentDatabaseOperator.addGroupMessage(thisGroupMessage);
        groupMessageRecord.clear();
        for (FriendMessage thisFriendMessage : friendMessageRecord)
            ConstantPool.Database.currentDatabaseOperator.addFriendMessage(thisFriendMessage);
        friendMessageRecord.clear();
    }

    private String encodeString(String input) {
        return UrlEncoded.encodeString(input);
    }
}
