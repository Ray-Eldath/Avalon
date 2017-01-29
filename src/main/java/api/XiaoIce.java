package api;

import org.json.JSONObject;
import tool.Response;

import java.util.List;
import java.util.Vector;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class XiaoIce implements API {
    @Override
    public void doPost(JSONObject object) {
        Response.responseGroup(object.getString("group_uid"), object.getString("content"));
        List<String> keywords = new Vector<>();
        keywords.add("Avalon");
        keywords.add("阿瓦隆");
        MainServlet.configure(keywords, this);
    }

    @Override
    public void response(String groupNumber) {
    }
}
