package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class FriendMessage implements Message {
    private final static Logger logger = LoggerFactory.getLogger(FriendMessage.class);
    private final int id;
    private final LocalDateTime time;
    private final long senderUid, timeLong;
    private final String senderNickName;
    private final String content;

    public FriendMessage(int id, LocalDateTime time, long senderUid, String senderNickName, String content) {
        this.id = id;
        this.time = time;
        this.timeLong = time.toEpochSecond(ZoneOffset.of("Asia/Shanghai"));
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
        logger.warn("Unsupported operation.",
                new UnsupportedOperationException("Unsupported operation: reply friend message."));
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
