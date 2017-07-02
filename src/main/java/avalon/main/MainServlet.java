package avalon.main;

import avalon.friend.MainFriendMessageHandler;
import avalon.group.MainGroupMessageHandler;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(MainServlet.class);

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        JSONObject object = (JSONObject) new JSONTokener(req.getReader()).nextValue();
        if (object.isNull("post_type") || object.isNull("type")) return;
        if (!"receive_message".equals(object.getString("post_type")))
            return;
        //
        long timeLong = object.getLong("time");
        int Id = object.getInt("id");
        long senderUid;
        if (object.isNull("sender_uid")) {
            logger.error("\"sender_uid\" is null! \n " +
                    "That means the your Mojo-Webqq is outdated or " +
                    "Tencent delete some API. If this problem still exist, " +
                    "try update Mojo-Webqq or new issues at:" +
                    " https://github.com/sjdy521/Mojo-Webqq/issues.");
            senderUid = 1234567890;
        } else senderUid = object.getLong("sender_uid");
        String sender = object.get("sender").toString();
        String content = object.get("content").toString();
        String type = object.getString("type");
        if (!("friend_message".equals(type) || "group_message".equals(type)))
            return;
        if ("friend_message".equals(type)) {
            FriendMessage message = new FriendMessage(Id, timeLong, senderUid, sender, content);
            if (!MessageChecker.check(message)) return;
            MainFriendMessageHandler.getInstance().handle(message);
        } else {
            long groupUid;
            if (object.isNull("group_uid")) {
                logger.error("\"group_uid\" is null! \n " +
                        "That means the your Mojo-Webqq is outdated or " +
                        "Tencent delete some API. If this problem still exist, " +
                        "try update Mojo-Webqq or new issues at:" +
                        " https://github.com/sjdy521/Mojo-Webqq/issues.");
                groupUid = 1234567891;
            } else groupUid = object.getLong("sender_uid");
            String group = object.get("group").toString();
            MainGroupMessageHandler.getInstance().handle(new GroupMessage(Id, timeLong,
                    senderUid, sender, groupUid, group, content));
        }
    }
}