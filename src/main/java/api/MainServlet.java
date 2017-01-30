package api;

import org.json.JSONObject;
import org.json.JSONTokener;
import tool.APIRateLimit;
import tool.APISurvivePool;
import tool.Response;
import tool.VariablePool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServlet extends HttpServlet {
    private static Map<String, API> apiList = new LinkedHashMap<>();
    public static final String[] followGroup = {"617118724"};
    static final String[] followPeople = {"951394653", "360736041", "1464443139", "704639565"};

    private static APIRateLimit cooling = new APIRateLimit(3000L);

    public MainServlet() {
        // CUSTOM 注意：此处configure的顺序决定优先级。
        // 开发者非常不建议修改此处内容，容易造成奇怪的问题。
        MainServlet.configure("avalon apimanager ", APIManager.getInstance());
        MainServlet.configure("avalon blacklist ", Blacklist.getInstance());
        MainServlet.configure("avalon help", Help.getInstance());
        MainServlet.configure("avalon version", Version.getInstance());
        MainServlet.configure(Mo.keywords, Mo.getInstance());
        MainServlet.configure("avalon echo ", Echo.getInstance());
        MainServlet.configure(XiaoIce.keywords, XiaoIce.getInstance());
    }


    static API getAPIByKeyword(String keyword) {
        if (!apiList.containsKey(keyword)) return null;
        return apiList.get(keyword);
    }

    private static void configure(String keyWord, API api) {
        apiList.put(keyWord.toLowerCase(), api);
        APISurvivePool.getInstance().addAPI(api);
    }

    private static void configure(List<String> keywords, API API) {
        for (String thisKeyWord : keywords)
            configure(thisKeyWord, API);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject object = (JSONObject) new JSONTokener(req.getInputStream()).nextValue();
        if (!("receive_message".equals(object.getString("post_type"))) &&
                "group_message".equals(object.getString("type")))
            return;
        String groupUid = object.get("group_uid").toString();
        String sender = object.get("sender").toString();
        String lowerContent = object.getString("content").toLowerCase();
        long time = object.getLong("time");
        for (String thisFollowGroup : followGroup)
            if (groupUid.equals(thisFollowGroup)) {
                for (Map.Entry<String, API> stringAPIEntry : apiList.entrySet()) {
                    String key = stringAPIEntry.getKey();
                    API value = stringAPIEntry.getValue();
                    if (lowerContent.contains(key)) {
                        if (!cooling.trySet(time)) {
                            if (!VariablePool.Limit_Noticed) {
                                Response.responseGroup(groupUid, "@" + sender +
                                        " 对不起，您的指令超频。3s内仅能有一次指令输入，未到2s内的输入将被忽略。" +
                                        "注意：此消息仅会显示一次。");
                                VariablePool.Limit_Noticed = true;
                            }
                            return;
                        }
                        if (!APISurvivePool.getInstance().isSurvive(value)) {
                            if (!APISurvivePool.getInstance().isNoticed(value)) {
                                Response.responseGroup(groupUid, "@" + sender +
                                        " 对不起，您调用的方法目前已被停止；注意：此消息仅会显示一次。");
                                APISurvivePool.getInstance().setNoticed(value);
                            }
                            return;
                        } else {
                            value.doPost(object);
                            return;
                        }
                    }
                }
            }
    }
}
