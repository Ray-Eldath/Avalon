package util;

import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static tool.ConstantPool.Address.APIServer;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class GroupMessage implements Message {
    private final static Logger logger = LoggerFactory.getLogger(GroupMessage.class);
    private final int id;
    private final LocalDateTime time;
    private final long senderUid, groupUid, timeLong;
    private final String groupName, content, senderNickName;

    public GroupMessage(int id, LocalDateTime time, long senderUid, String senderNickName, long groupUid,
                        String groupName, String content) {
        this.id = id;
        this.time = time;
        this.timeLong = time.toEpochSecond(ZoneOffset.of("Asia/Shanghai"));
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
        try {
            new URL(APIServer + "/openqq/send_group_message?uid=" + groupUid + "&content=" +
                    UrlEncoded.encodeString(reply)).openStream();
        } catch (IOException e) {
            logger.warn("IOException thrown while response group message: ", e);
        }
    }

    public void response(String reply, int shutUpTime) {
        response(reply);
        try {
            new URL(APIServer + "/openqq/shutup_group_member?time=" + shutUpTime +
                    "&group_uid=" + groupUid +
                    "&member_uid=" + senderUid).openStream();
        } catch (IOException e) {
            logger.warn("IOException thrown while response group message and shut up: ", e);
        }
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
}
