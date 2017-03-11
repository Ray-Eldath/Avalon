package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class FriendMessage implements Message {
    private static Logger logger = LoggerFactory.getLogger(FriendMessage.class);
    private int id;
    private LocalDateTime time;
    private long senderUid;
    private String senderNickName, content;

    public FriendMessage(int id, LocalDateTime time, long senderUid, String senderNickName, String content) {
        this.id = id;
        this.time = time;
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
