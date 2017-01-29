package api;

import org.json.JSONObject;
import tool.Response;

import java.net.URLEncoder;

import static api.MainServlet.followPeople;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class About implements API {
    private static About instance = null;

    static API getInstance() {
        if (instance == null) instance = new About();
        return instance;
    }

    @Override
    public void doPost(JSONObject object) {
        String group_uid = object.get("group_uid").toString();
        String sender_uid = object.getString("sender_uid");
        for (String thisFollowPeople : followPeople)
            if (thisFollowPeople.equals(sender_uid)) {
                response(group_uid);
                return;
            }
        MainServlet.configure("version", this);
    }

    @Override
    public void response(String groupNumber) {
        try {
            String message = URLEncoder.encode(
                    "Hi, I'm Avalon.\n" +
                            "我是阿瓦隆，QQ群机器人。\n" +
                            "我的名字和头像均取自《Implosion》，我的父亲是Mojo-Webqq。\n" +
                            "我由Eldath Ray进行二次开发。\n" +
                            "Mojo-Webqq Version: v2.0.4\tAvalon Version: v0.0.1 Pre-Alpha", "utf-8");
            Response.responseGroup(groupNumber, message);
        } catch (Exception ignore) {
        }
    }
}
