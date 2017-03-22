package util;

import java.time.LocalDateTime;

/**
 * Created by Eldath on 2017/2/12 0012.
 *
 * @author Eldath
 */
public interface Message {
    String getContent();

    LocalDateTime getTime();

    long getTimeLong();

    int getId();

    long getSenderUid();

    String getSenderNickName();

    void response(String reply);
}
