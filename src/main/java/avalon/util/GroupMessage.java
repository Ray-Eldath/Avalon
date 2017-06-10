package avalon.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.logging.Logger;

import static avalon.tool.pool.ConstantPool.Basic.currentServlet;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class GroupMessage implements Message, Displayable {
    private final static Logger logger = Logger.getGlobal();
    private final int id;
    private final LocalDateTime time;
    private final long senderUid, groupUid, timeLong;
    private final String groupName, content, senderNickName;

    public GroupMessage(int id, LocalDateTime time, long senderUid, String senderNickName, long groupUid,
                        String groupName, String content) {
        this.id = id;
        this.time = time;
        this.timeLong = time.toEpochSecond(ZoneOffset.ofHours(Calendar.ZONE_OFFSET));
        this.senderUid = senderUid;
        this.groupUid = groupUid;
        this.senderNickName = senderNickName;
        this.groupName = groupName;
        this.content = content;
    }

    public GroupMessage(int id, long time, long senderUid, String senderNickName, long groupUid,
                        String groupName, String content) {
        this.id = id;
        this.timeLong = time;
        this.time = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.of("Asia/Shanghai"));
        this.senderUid = senderUid;
        this.groupUid = groupUid;
        this.senderNickName = senderNickName;
        this.groupName = groupName;
        this.content = content;
    }

    @Override
    public void response(String reply) {
        currentServlet.responseGroup(groupUid, reply);
    }

    public void response(String reply, int shutUpTime) {
        response(reply);
        currentServlet.shutUp(groupUid, senderUid, shutUpTime);
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


    public long getGroupUid() {
        return groupUid;
    }

    @Override
    public String getSenderNickName() {
        return senderNickName;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getString() {
        return String.format("id= % 6d, time= %-18s, senderUid= % 12d, senderNickName= \"%-15s\", groupUid= % 10d, " +
                        "groupName= \"%-18s\", content= \"%s\"", id, time.toString().replace("T", " "),
                senderUid, senderNickName, groupUid, groupName, content);
    }
}
