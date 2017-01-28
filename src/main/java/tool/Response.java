package tool;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Response {
    public static void responseFriend(String friendNumber, String content) {
        try {
            new URL("http://127.0.0.1:5000/openqq/send_friend_message?uid=" + friendNumber + "&content=" +
                    URLEncoder.encode(content, "utf-8")).openConnection().connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void responseGroup(String groupNumber, String content) {
        try {
            new URL("http://127.0.0.1:5000/openqq/send_group_message?uid=" + groupNumber + "&content=" +
                    URLEncoder.encode(content, "utf-8")).openConnection().connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
