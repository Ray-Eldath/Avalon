package api;

import org.json.JSONObject;
import org.json.JSONTokener;

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

    protected static void configure(String keyWord, API API) {
        apiList.put(keyWord.toLowerCase(), API);
    }

    protected static void configure(List<String> keywords, API API) {
        for (String thisKeyWord : keywords)
            configure(thisKeyWord, API);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONTokener tokener = new JSONTokener(req.getInputStream());
        JSONObject object = (JSONObject) tokener.nextValue();
        if (!(object.getString("post_type").equals("receive_message")) ||
                !(object.getString("type").equals("group_message")))
            return;
        String group_uid = object.get("group_uid").toString();
        String lowerContent = object.getString("content").toLowerCase();
        for (String thisFollowGroup : followGroup)
            if (group_uid.equals(thisFollowGroup)) {
                for (Map.Entry<String, API> stringAPIEntry : apiList.entrySet()) {
                    String key = (String) ((Map.Entry) stringAPIEntry).getKey();
                    API value = (API) ((Map.Entry) stringAPIEntry).getValue();
                    if (lowerContent.contains(key)) {
                        value.doPost(object);
                        return;
                    }
                }
            }
    }
}
