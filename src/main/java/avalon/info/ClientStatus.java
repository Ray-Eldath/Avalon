package avalon.info;

import avalon.tool.pool.ConstantPool;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Eldath Ray on 2017/4/18 0018.
 * <p>
 * Usage: ../avalon/v0/info/get_client_status
 *
 * @author Eldath Ray
 */
public class ClientStatus extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        JSONObject object = new JSONObject();
        JSONObject webqq = new JSONObject(), wechat = new JSONObject();
        try {
            webqq = (JSONObject) new JSONTokener(new URL(ConstantPool.Address.servlet +
                    "/openqq/get_client_info").openStream()).nextValue();
            wechat = (JSONObject) new JSONTokener(new URL(ConstantPool.Address.wechat +
                    "/openwx/get_client_info").openStream()).nextValue();
        } catch (IOException ignore) {
        }
        JSONObject avalon = new JSONObject();
        object.put("servlet", webqq);
        object.put("wechat", wechat);
        avalon.put("code", 0);
        avalon.put("pid", ConstantPool.Basic.pid);
        avalon.put("starttime", ConstantPool.Basic.startTime);
        avalon.put("runtime", System.currentTimeMillis() - ConstantPool.Basic.startTime);
        avalon.put("version", ConstantPool.Version.avalon);
        avalon.put("debug", ConstantPool.Basic.debug);
        object.put("avalon", avalon);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(object.toString());
    }
}
