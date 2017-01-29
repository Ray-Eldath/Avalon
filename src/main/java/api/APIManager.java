package api;

import org.json.JSONObject;
import tool.APISurvivePool;
import tool.Response;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APIManager implements API {
    private static APIManager instance = null;

    public static APIManager getInstance() {
        if (instance == null) instance = new APIManager();
        return instance;
    }

    @Override
    public void doPost(JSONObject object) {
        String content = object.get("content").toString();
        String sender = object.get("sender").toString();
        String sender_uid = object.get("sender_uid").toString();
        String group_uid = object.get("group_uid").toString();
        String action;
        String[] split;
        for (String thisFollowFriend : MainServlet.followPeople)
            if (sender_uid.equals(thisFollowFriend)) {
                if (!content.contains(" ")) {
                    Response.responseGroup(group_uid, "@" + sender + " 您的指示格式不对辣！（｀Δ´）！");
                    return;
                }
                split = content.split(" ");
                API thisAPI = MainServlet.getAPIByKeyword(split[3]);
                action = split[2];
                if (thisAPI == null || APISurvivePool.getInstance().containAPI(thisAPI)) {
                    Response.responseGroup(group_uid, "@" + sender + " 您要操作的API根本不存在！(╯︵╰,)");
                    return;
                }
                if (action.equals("start")) {
                    APISurvivePool.getInstance().setAPISurvive(thisAPI, true);
                    Response.responseGroup(group_uid, "@" + sender + " 您要重启的API将会重启`(*∩_∩*)′");
                    return;
                }
                if (action.equals("stop")) {
                    APISurvivePool.getInstance().setAPISurvive(thisAPI, false);
                    Response.responseGroup(group_uid, "@" + sender + " 您要关闭的API将会关闭~");
                    return;
                } else {
                    Response.responseGroup(group_uid, "@" + sender + " 您的指示格式不对辣！（｀Δ´）！");
                    return;
                }
            }
    }

    @Override
    public void response(String groupNumber) {
    }
}
