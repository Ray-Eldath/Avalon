package tool;

import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Response {
    public static String responseXiaoIce(String content) {
        JSONTokener tokener;
        try {
            tokener = new JSONTokener(new URL("http://127.0.0.1:3500/openwx/consult?account=xiaoice-ms" +
                    "&content=" + UrlEncoded.encodeString(content)).openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        JSONObject object = (JSONObject) tokener.nextValue();
        if (object.isNull("reply")) return null;
        return object.getString("reply");
    }

    public static void responseFriend(String friendNumber, String content) {
        try {
            new URL("http://127.0.0.1:5000/openqq/send_friend_message?uid=" + friendNumber + "&content=" +
                    UrlEncoded.encodeString(content)).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void responseGroup(String groupNumber, String content) {
        try {
            new URL("http://127.0.0.1:5000/openqq/send_group_message?uid=" + groupNumber + "&content=" +
                    UrlEncoded.encodeString(content)).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
