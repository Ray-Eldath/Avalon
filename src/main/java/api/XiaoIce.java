package api;

import org.json.JSONObject;
import tool.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class XiaoIce implements API {
    private static XiaoIce instance = null;
    static List<String> keywords = new ArrayList<>();

    static {
        keywords.add("Avalon");
        keywords.add("阿瓦隆");
    }

    public static XiaoIce getInstance() {
        if (instance == null) instance = new XiaoIce();
        return instance;
    }


    @Override
    public void doPost(JSONObject object) {
        String content = object.get("content").toString();
        for (String thisKeyWord : keywords)
            content = content.replace(thisKeyWord.toLowerCase(), "小冰");
        String XiaoIce = Response.responseXiaoIce(content);
        if (XiaoIce == null) return;
        Response.responseGroup(object.get("group_uid").toString(), XiaoIce);
    }

    @Override
    public void response(String groupNumber) {
    }
}
