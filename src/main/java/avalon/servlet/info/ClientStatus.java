package avalon.servlet.info;

import avalon.tool.pool.Constants;
import org.json.JSONObject;
import org.json.JSONTokener;

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
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		JSONObject object = new JSONObject();
		JSONObject webqq = new JSONObject(), wechat = new JSONObject();
		try {
			webqq = (JSONObject) new JSONTokener(new URL(Constants.Address.servlet +
					"/openqq/get_client_info").openStream()).nextValue();
		   /* wechat = (JSONObject) new JSONTokener(new URL(Constants.Address.wechat +
                    "/openwx/get_client_info").openStream()).nextValue();*/
		} catch (IOException ignore) {
		}
		JSONObject avalon = new JSONObject();
		object.put("servlet", webqq);
		object.put("wechat", wechat);
		avalon.put("code", 0);
		avalon.put("pid", Constants.Basic.pid);
		avalon.put("starttime", Constants.Basic.startTime);
		avalon.put("runtime", System.currentTimeMillis() - Constants.Basic.startTime);
		avalon.put("version", Constants.Version.avalon);
		avalon.put("debug", Constants.Basic.debug);
		object.put("avalon", avalon);
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.getWriter().print(object.toString());
	}
}
