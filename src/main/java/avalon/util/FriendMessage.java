package avalon.util;

import avalon.tool.pool.ConstantPool;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;

import static avalon.tool.pool.ConstantPool.Basic.currentServlet;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class FriendMessage implements Message, Displayable {
    private final int id;
    private final LocalDateTime time;
    private final long senderUid, timeLong;
    private final String senderNickName;
    private final String content;

    public FriendMessage(int id, LocalDateTime time, long senderUid, String senderNickName, String content) {
        this.id = id;
        this.time = time;
        this.timeLong = time.toEpochSecond(ZoneOffset.ofHours(Calendar.ZONE_OFFSET));
        this.senderUid = senderUid;
        this.senderNickName = senderNickName;
        this.content = content;
    }

    public FriendMessage(int id, long time, long senderUid, String senderNickName, String content) {
        this.id = id;
        this.time = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.of("Asia/Shanghai"));
        this.timeLong = time;
        this.senderUid = senderUid;
        this.senderNickName = senderNickName;
        this.content = content;
    }

    @Override
    public void response(String reply) {
        if (ConstantPool.Basic.localOutput)
            System.out.println("Friend output: " + reply);
        else
            currentServlet.responseFriend(senderUid, reply);
    }

    @Override
    public String getString() {
        return "id=" + id + "\ttime=" + time + "\tsenderUid=" + senderUid + "\tsenderNickName=\"" +
                senderNickName + "\"\tcontent=\"" + content + "\"";
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
    public long getTimeLong() {
        return timeLong;
    }

    @Override
    public long getSenderUid() {
        return senderUid;
    }

    @Override
    public String getSenderNickName() {
        return senderNickName;
    }

    @Override
    public String getContent() {
        return content;
    }
}
