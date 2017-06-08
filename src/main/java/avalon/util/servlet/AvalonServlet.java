package avalon.util.servlet;

import avalon.util.FriendMessage;
import avalon.util.GroupMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Consumer;

/**
 * Created by Eldath Ray on 2017/6/9 0009.
 *
 * @author Eldath Ray
 */
public interface AvalonServlet {
    void responseGroup(long groupUid, String reply);

    void responseFriend(long friendUid, String reply);

    void shutUp(long groupUid, long senderUid, long time);

    void shutdown();

    default void clean() {
        System.out.println("No clean needed.");
    }

    String getGroupSenderNickname(long groupUid, long senderUid);

    String getFriendSenderNickname(long uid);

    void setGroupMessageReceivedHook(Consumer<GroupMessage> hook);

    void setFriendMessageReceivedHook(Consumer<FriendMessage> hook);

    void doPost(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
