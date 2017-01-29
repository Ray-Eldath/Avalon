package tool;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Response {
    public static String responseXiaoIce(String content) {
        JSONTokener tokener;
        try {
            tokener = new JSONTokener(new URL("http://127.0.0.1:3500/openwx/consult?account=ms-xiaoice" +
                    "&content=" + URLEncoder.encode(content, "utf-8")).openStream());
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
                    URLEncoder.encode(content, "utf-8")).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void responseGroup(String groupNumber, String content) {
        try {
            new URL("http://127.0.0.1:5000/openqq/send_group_message?uid=" + groupNumber + "&content=" +
                    URLEncoder.encode(content, "utf-8")).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
