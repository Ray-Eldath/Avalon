package avalon.tool;

import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);
    private static final List<String> replaceList = new ArrayList<>();

    static {
        replaceList.add("小怪冰");
        replaceList.add("小冰冰");
        replaceList.add("冰酱");
        replaceList.add("小冰");
    }

    private Response() {
    }

    public static String responseXiaoIce(String content) {
        JSONTokener tokener;
        try {
            tokener = new JSONTokener(new URL("http://127.0.0.1:3500/openwx/consult?account=xiaoice-ms" +
                    "&content=" + UrlEncoded.encodeString(content)).openStream());
        } catch (IOException e) {
            logger.warn("IOException thrown while responseXiaoIce: ", e);
            return null;
        }
        JSONObject object = (JSONObject) tokener.nextValue();
        if (object.isNull("reply")) return null;
        String reply;
        try {
            reply = new String(object.get("reply").toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            return null;
        }
        if (reply.contains("[语音]")) return null;
        else if (reply.contains("[图片]")) return null;
        for (String thisReplaceWord : replaceList)
            reply = reply.replace(thisReplaceWord, "Avalon");
        return reply;

    }

    /**
     * @deprecated Since 0.0.1-beta, this method is replaced by {@link avalon.util.FriendMessage#response(String)}.
     */
    public static void responseFriend(long friendNumber, String content) {
        try {
            new URL("http://127.0.0.1:5000/openqq/send_friend_message?uid=" + friendNumber + "&content=" +
                    UrlEncoded.encodeString(content)).openStream();
        } catch (IOException e) {
            logger.warn("IOException thrown while responseFriend: ", e);
        }
    }

    /**
     * @see avalon.util.GroupMessage#response(String) and {@link avalon.util.GroupMessage#response(String, int)}.
     */
    public static void sendToGroup(long groupNumber, String content) {
        try {
            new URL("http://127.0.0.1:5000/openqq/send_group_message?uid=" + groupNumber + "&content=" +
                    UrlEncoded.encodeString(content)).openStream();
        } catch (IOException e) {
            logger.warn("IOException thrown while sendToGroup: ", e);
        }
    }
}
