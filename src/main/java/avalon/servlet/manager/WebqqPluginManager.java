package avalon.servlet.manager;

import avalon.tool.pool.OnlineWebqqPluginPool;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Eldath Ray on 2017/4/20 0020.
 * <p>
 * Usage: .../avalon/v0/manager/webqq_plugin_manager?target={Plugin's Name}
 * &operation={get_all_info|set_parameter|disable|enabled}
 * If the {@code operation} is {@code set_parameter}, than the URL should add:
 * ..&key={PluginParameter's name}&value={Value to set}
 * If the {@code operation} is {@code enabled} or {@code set_parameter},
 * then <strong>must use POST to post the parameters to the server</strong>.
 * <p>
 * JSON format to POST when operation is {@code set_parameter}:
 * {@code {
 * "plugin_name":"{The plugin you want to set parameter}",
 * "parameter": {
 * "{The name of the parameter}":{The value of the parameter}
 * }
 * }}
 * <p>
 * JSON format to POST when operation is {@code enabled}:
 * {@code {
 * "plugin_name":"{The plugin you want to enabled}",
 * "parameters": {
 * "{The name of the parameter}":{The value of the parameter}
 * }
 * }}
 *
 * @author Eldath Ray
 */
public class WebqqPluginManager extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String target = req.getParameter("target");
        String operation = req.getParameter("operation");
        JSONObject object;
        if (!preCheck(req, resp))
            return;
        //
        if ("get_all_info".equals(operation)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(WebqqPluginManagerHandler.getInstance().getInfo(target));
            return;
        }
        if ("disable".equals(operation)) {
            object = WebqqPluginManagerHandler.getInstance().disabled(target);
            if (object.has("error"))
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            else
                resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(object.toString());
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().print(new JSONObject().put("error", "调用格式不正确。").toString());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String target = req.getParameter("target");
        String operation = req.getParameter("operation");
        JSONObject object;
        JSONObject postObject = (JSONObject) new JSONTokener(req.getReader()).nextValue();
        if (!preCheck(req, resp))
            return;
        //FIXME 需要整个LocalWebqqPluginPool再写。今天先不玩了。
        // Plugin plugin = OnlineWebqqPluginPool.getInstance().getPlugin(target);
        if ("set_parameter".equals(operation)) {
            JSONObject parameters = postObject.getJSONObject("parameters");

        }
    }

    private boolean preCheck(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String target = req.getParameter("target");
        String operation = req.getParameter("operation");
        if (target == null || operation == null || "".equals(target) || "".equals(operation)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(new JSONObject().put("error", "调用格式不正确。").toString());
            return false;
        }
        if (!OnlineWebqqPluginPool.getInstance().isPluginExist(target)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(new JSONObject().put("error", "指定的插件" + target + "不存在。").toString());
            return false;
        }
        return true;
    }
}
