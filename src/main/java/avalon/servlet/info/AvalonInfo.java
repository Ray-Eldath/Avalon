package avalon.servlet.info;

import avalon.extend.Recorder;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 * <p>
 * Usage: ../avalon/v0/info/get_avalon_info/target={current_recorded_message_count}
 *
 * @author Eldath Ray
 */
public class AvalonInfo extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String target = req.getParameter("target");
        if ("current_recorded_message_count".equals(target))
            response(currentRecordedMessageCount(), resp);
    }

    private void response(String content, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(content);
    }

    private String currentRecordedMessageCount() {
        JSONObject object = new JSONObject(), group = new JSONObject(), friend = new JSONObject();
        group.put("total", Recorder.getNowGroupCount());
        group.put("max", Recorder.getMaxRecordGroupMessageCount());
        object.put("group", group);
        friend.put("total", Recorder.getNowFriendCount());
        friend.put("max", Recorder.getMaxRecordFriendMessageCount());
        object.put("friend", friend);
        return object.toString();
    }
}
