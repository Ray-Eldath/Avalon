package api;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.LoggerFactory;
import tool.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServlet extends HttpServlet {
    private static final String[] followGroup = {"399863405"};

    static final String[] followPeople = {"951394653", "360736041", "1464443139", "704639565"};

    protected void configure(String keyWord, API API) {
        //TODO 到时候直接调用这个，就不用像下面那么麻烦了。不过等我整完bug再说。你们谁都别写这里！ - Eldath
    }

    protected void configure(List<String> keywords, API API) {
        //TODO 同上个TODO
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
            Response.responseGroup(thisFollowGroup, "Avalon已经上线。");
        // here
        LoggerFactory.getLogger(MainServlet.class).info("Client connect: " + req.getSession().getId());
        for (String thisFollowGroup : followGroup)
            if (group_uid.equals(thisFollowGroup)) {
                if (lowerContent.contains("version"))
                    About.getInstance().doPost(object);
                for (String thisKeyWord : Mo.keyWords)
                    if (lowerContent.contains(thisKeyWord)) {
                        Mo.getInstance().doPost(object);
                    }
            }
    }
}
