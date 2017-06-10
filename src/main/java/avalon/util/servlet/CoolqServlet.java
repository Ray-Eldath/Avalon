package avalon.util.servlet;

import avalon.tool.pool.ConstantPool;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;


/**
 * Created by Eldath Ray on 2017/6/10 0010.
 *
 * @author Eldath Ray
 */
public class CoolqServlet extends AvalonServlet {
    private static final Logger logger = LoggerFactory.getLogger(CoolqServlet.class);
    private static int friendMessageId = 0;
    private static int groupMessageId = 0;
    private Consumer<GroupMessage> groupMessageConsumer;
    private Consumer<FriendMessage> friendMessageConsumer;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        JSONObject object = ((JSONObject) (new JSONTokener(req.getInputStream()).nextValue())).getJSONObject("data");
        if (!"message".equals(object.getString("post_type")))
            return;
        String messageType = object.getString("message_type");
        if ("private".equals(messageType) && "friend".equals(object.getString("friend"))) {
            long senderUid = object.getLong("user_id");
            friendMessageConsumer.accept(new FriendMessage(
                    friendMessageId++,
                    System.currentTimeMillis(),
                    senderUid,
                    getFriendSenderNickname(senderUid),
                    object.getString("message")
            ));
        } else if ("group".equals(messageType)) {
            if (object.get("anonymous") != null)
                return;
            long groupUid = object.getLong("group_id");
            long senderUid = object.getLong("user_id");
            groupMessageConsumer.accept(new GroupMessage(
                    groupMessageId++,
                    System.currentTimeMillis(),
                    senderUid,
                    getGroupSenderCardName(groupUid, senderUid),
                    groupUid,
                    getGroupName(groupUid),
                    object.getString("message")
            ));
        }
    }

    private String getGroupName(long groupUid) {
        List<Object> list = ((JSONObject) new JSONTokener(sendRequest("/get_group_list", null))
                .nextValue()).getJSONObject("data").getJSONArray("").toList();
        for (Object obj : list) {
            JSONObject object = (JSONObject) obj;
            if (object.getLong("group_id") == groupUid)
                return object.getString("group_name");
        }
        return "ERROR - UNKNOWN";
    }

    @Override
    public void responseGroup(long groupUid, String reply) {
        if (ConstantPool.Basic.Debug)
            System.out.println("Group output: " + reply);
        else {
            Map<String, Object> object = new HashMap<>();
            object.put("group_id", groupUid);
            object.put("message", reply);
            object.put("is_raw", true); //TODO 未来若提供图片支持需修改这里
            sendRequest("/send_group_msg", object);
        }
    }

    @Override
    public void responseFriend(long friendUid, String reply) {
        if (ConstantPool.Basic.Debug)
            System.out.println("Friend output: " + reply);
        else {
            Map<String, Object> object = new HashMap<>();
            object.put("user_id", friendUid);
            object.put("message", reply);
            object.put("is_raw", true); //TODO 未来若提供图片支持需修改这里
            sendRequest("/send_private_msg", object);
        }
    }

    @Override
    public void shutUp(long groupUid, long userUid, long time) {
        Map<String, Object> object = new HashMap<>();
        object.put("group_id", groupUid);
        object.put("user_id", userUid);
        object.put("duration", time);
        sendRequest("/set_group_ban", object);
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("please shutdown manually");
    }

    @Override
    public String name() {
        return "Cooq";
    }

    @Override
    public String getGroupSenderCardName(long groupUid, long userUid) {
        Map<String, Object> object = new HashMap<>();
        object.put("group_id", groupUid);
        object.put("user_id", userUid);
        System.out.println(object.toString());
        System.out.println();
        System.out.println(new JSONTokener(sendRequest("/get_group_member_info", object)).nextValue());
        return ((JSONObject) (new JSONTokener(sendRequest("/get_group_member_info", object))
                .nextValue())).getJSONObject("data").getString("card");
    }

    @Override
    public String getFriendSenderNickname(long uid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", uid);
        return ((JSONObject) (new JSONTokener(sendRequest("/get_stranger_info", map))
                .nextValue())).getJSONObject("data").getString("nickname");
    }

    @Override
    public void setGroupMessageReceivedHook(Consumer<GroupMessage> hook) {
        this.groupMessageConsumer = hook;
    }

    @Override
    public void setFriendMessageReceivedHook(Consumer<FriendMessage> hook) {
        this.friendMessageConsumer = hook;
    }

    private String sendRequest(String url, Map<String, Object> data) {
        String requestString;
        if (data == null)
            requestString = "";
        else {
            StringBuilder request = new StringBuilder();
            Set<Map.Entry<String, Object>> entrySet = data.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                String key = entry.getKey();
                Object value = entry.getValue();
                request.append(key).append("=");
                if (value instanceof String)
                    request.append(UrlEncoded.encodeString((String) value));
                else
                    request.append(String.valueOf(value));
                request.append("&");
            }
            String temp = request.toString();
            requestString = temp.substring(0, temp.length() - 1);
        }
        StringBuilder response = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(
                    apiAddress() + url + "?" + requestString).openStream()));
            String thisLine;
            while ((thisLine = reader.readLine()) != null)
                response.append(thisLine);
            reader.close();
        } catch (Exception e) {
            logger.error("exception thrown while sendRequest to " + url + " " + e);
        }
        return response.toString();
    }
}
