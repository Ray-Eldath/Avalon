package api;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.LoggerFactory;
import tool.APISurvivePool;
import tool.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServlet extends HttpServlet {
    public static final String[] followGroup = {"399863405"};
    private static Map<String, API> apiList = new HashMap<>();
    static final String[] followPeople = {"951394653", "360736041", "1464443139", "704639565"};

    public MainServlet() {
        MainServlet.configure("avalon version", Version.getInstance());
        MainServlet.configure(Mo.keywords, Mo.getInstance());
        MainServlet.configure(XiaoIce.keywords, XiaoIce.getInstance());
        MainServlet.configure("avalon help", Help.getInstance());
        MainServlet.configure("avalon APIManager ", APIManager.getInstance());
    }


    public static API getAPIByKeyword(String keyword) {
        if (!apiList.containsKey(keyword)) return null;
        return apiList.get(keyword);
    }

    static void configure(String keyWord, API api) {
        apiList.put(keyWord.toLowerCase(), api);
        APISurvivePool.getInstance().addAPI(api);
    }

    static void configure(List<String> keywords, API API) {
        for (String thisKeyWord : keywords)
            configure(thisKeyWord, API);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject object = (JSONObject) new JSONTokener(req.getInputStream()).nextValue();
        if (!(object.getString("post_type").equals("receive_message") &&
                object.getString("type").equals("group_message")))
            return;
        String group_uid = object.get("group_uid").toString();
        String sender = object.get("sender").toString();
        String lowerContent = object.getString("content").toLowerCase();
        LoggerFactory.getLogger(MainServlet.class).info("message in " + group_uid + " with " + lowerContent);
        for (String thisFollowGroup : followGroup)
            if (group_uid.equals(thisFollowGroup)) {
                for (Map.Entry<String, API> stringAPIEntry : apiList.entrySet()) {
                    String key = (String) ((Map.Entry) stringAPIEntry).getKey();
                    API value = (API) ((Map.Entry) stringAPIEntry).getValue();
                    if (!APISurvivePool.getInstance().isSurvive(value)) {
                        Response.responseGroup(group_uid, "@" + sender + " 对不起，您调用的方法目前已被禁止。");
                        return;
                    }
                    if (lowerContent.contains(key)) {
                        value.doPost(object);
                        return;
                    }
                }
            }
    }
}
