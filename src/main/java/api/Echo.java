package api;

import org.json.JSONObject;
import tool.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Echo implements API {
    private static Echo instance = null;
    private static List<String> allowList = new ArrayList<>();

    static {
        allowList.add("1464443139");
        allowList.add("951394653");
    }

    static Echo getInstance() {
        if (instance == null) instance = new Echo();
        return instance;
    }

    @Override
    public void doPost(JSONObject object) {
        String content = object.get("content").toString();
        String sender_uid = object.get("sender_uid").toString();
        String group_uid = object.get("group_uid").toString();
        String sender = object.get("sender").toString();
        if (!content.contains(" ")) {
            Response.responseGroup(group_uid, "您的指示恕我不能遵守⊙﹏⊙! 因为不合规范嘛(╯︵╰,)");
            return;
        }
        for (String thisAllow : allowList)
            if (sender_uid.equals(thisAllow)) {
                Response.responseGroup(group_uid, content.split(" ")[2]);
                return;
            }
        Response.responseGroup(group_uid, "@" + sender + " 您没有权限欸... ...(゜д゜)");
    }

    @Override
    public void response(String groupNumber) {

    }
}
