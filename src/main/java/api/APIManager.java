package api;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.APISurvivePool;
import tool.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APIManager implements API {
    private static APIManager instance = null;
    private static Logger logger = LoggerFactory.getLogger(APIManager.class);
    private static List<String> stopAllowUid = new ArrayList<>();
    private static List<String> startAllowUid = new ArrayList<>();

    static {
        // CUSTOM 以下为允许关闭API的QQ号
        stopAllowUid.add("1464443139");
        stopAllowUid.add("360736041");
        stopAllowUid.add("704639565");
        stopAllowUid.add("951394653");
        stopAllowUid.add("1016281105");
        // CUSTOM 以下为允许重启API的QQ号
        startAllowUid.add("1464443139");
        startAllowUid.add("704639565");
        startAllowUid.add("951394653");
    }

    static APIManager getInstance() {
        if (instance == null) instance = new APIManager();
        return instance;
    }

    @Override
    public void doPost(JSONObject object) {
        String content = object.get("content").toString();
        String sender = object.get("sender").toString();
        String sender_uid = object.get("sender_uid").toString();
        String group_uid = object.get("group_uid").toString();
        String action, apiName;
        for (String thisFollowFriend : MainServlet.followPeople)
            if (sender_uid.equals(thisFollowFriend)) {
                if (!content.contains(" ")) {
                    Response.responseGroup(group_uid, "@" +
                            sender + " 您的指示格式不对辣！（｀Δ´）！请注意在API触发语句后是否缺少空格~");
                    return;
                }
                apiName = content.toLowerCase().
                        replace("avalon apimanager stop ", "").replace("avalon apimanager start ", "");
                API thisAPI = MainServlet.getAPIByKeyword(apiName);
                action = content.toLowerCase().
                        replace("avalon apimanager ", "").replace(apiName, "").trim();
                if (thisAPI == null) {
                    Response.responseGroup(group_uid, "@" + sender + " 您要操作的API根本不存在！(╯︵╰,)");
                    return;
                }
                if (action.equals("start")) {
                    for (String thisAllowStartUid : startAllowUid)
                        if (thisAllowStartUid.equals(sender_uid)) {
                            APISurvivePool.getInstance().setAPISurvive(thisAPI, true);
                            Response.responseGroup(group_uid, "@" + sender + " 您要重启的API将会重启`(*∩_∩*)′");
                            logger.info("API " + thisAPI.getClass().getName() + " is reopened by " +
                                    sender_uid + " : " + sender + ".");
                            return;
                        }
                }
                if (action.equals("stop")) {
                    for (String thisStopAllowUid : stopAllowUid) {
                        if (thisStopAllowUid.equals(sender_uid)) {
                            APISurvivePool.getInstance().setAPISurvive(thisAPI, false);
                            Response.responseGroup(group_uid, "@" + sender + " 您要关闭的API将会关闭~");
                            logger.info("API " + thisAPI.getClass().getName() + " is closed by " +
                                    sender_uid + " : " + sender + ".");
                            return;
                        }
                    }
                } else {
                    Response.responseGroup(group_uid, "@" + sender + " 您的指示格式不对辣！（｀Δ´）！");
                    return;
                }
            }
        Response.responseGroup(group_uid, "@" + sender + " 您没有权限啦！(゜д゜)");
    }

    @Override
    public void response(String groupNumber) {
    }
}
