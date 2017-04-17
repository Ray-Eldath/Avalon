package avalon.main;

import avalon.friend.MainFriendMessageHandler;
import avalon.group.MainGroupMessageHandler;
import avalon.tool.ConfigSystem;
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
        if ((boolean) ConfigSystem.getInstance().getConfig("Debug")) logger.info(object.toString());
        if (object.isNull("post_type") || object.isNull("type")) return;
        if (!"receive_message".equals(object.getString("post_type")))
            return;
        //
        long timeLong = object.getLong("time");
        int Id = object.getInt("id");
        long senderUid = object.getLong("sender_uid");
        String sender = object.get("sender").toString();
        String content = object.get("content").toString();
        String type = object.getString("type");
        if (!("friend_message".equals(type) || "group_message".equals(type)))
            return;
        if ("friend_message".equals(type)) {
            FriendMessage message = new FriendMessage(Id, timeLong, senderUid, sender, content);
            if (!MessageChecker.checkEncode(message)) return;
            MainFriendMessageHandler.getInstance().handle(message);
        } else {
            long groupUid = object.getLong("group_uid");
            String group = object.get("avalon/group").toString();
            GroupMessage message = new GroupMessage(Id, timeLong, senderUid, sender, groupUid, group, content);
            MainGroupMessageHandler.getInstance().handle(message);
        }
    }
}