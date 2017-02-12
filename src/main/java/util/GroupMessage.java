package util;

import java.time.LocalDateTime;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class GroupMessage implements Message {
    private int id;
    private LocalDateTime time;
    private long senderUid, receiverUid, groupUid;
    private String senderNickName, receiverNickName, groupName, content;

    public GroupMessage(int id, LocalDateTime time, long senderUid, String senderNickName, long receiverUid,
                        String receiverNickName, long groupUid, String groupName, String content) {
        this.id = id;
        this.time = time;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.groupUid = groupUid;
        this.senderNickName = senderNickName;
        this.receiverNickName = receiverNickName;
        this.groupName = groupName;
        this.content = content;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public long getSenderUid() {
        return senderUid;
    }

    public long getReceiverUid() {
        return receiverUid;
    }

    public long getGroupUid() {
        return groupUid;
    }

    @Override
    public String getSenderNickName() {
        return senderNickName;
    }

    public String getReceiverNickName() {
        return receiverNickName;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String getContent() {
        return content;
    }
}
