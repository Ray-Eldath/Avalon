package avalon.servlet.info;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Eldath Ray on 2017/4/18 0018.
 * <p>
 * Usage: GET .../avalon/v0/avalon.info/get_webqq_plugin_info
 * Return: https://raw.githubusercontent.com/Ray-Eldath/Avalon-Plugin-Info-Getter/master/plugins.json
 *
 * @author Eldath Ray
 */
public class WebqqPluginInfo extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        JSONObject object;
        JSONTokener tokener = new JSONTokener(new InputStreamReader((new URL("https://raw." +
                "githubusercontent.com/Ray-Eldath/Avalon-Plugin-Info-Getter/master/plugin_info.json").openStream())));
        object = (JSONObject) tokener.nextValue();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(object.toString());
    }
}
