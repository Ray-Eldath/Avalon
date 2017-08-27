package avalon.servlet.info;

import avalon.tool.pool.ConstantPool;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Eldath Ray on 2017/4/18 0018.
 * <p>
 * Usage: ../avalon/v0/info/get_client_version
 *
 * @author Eldath Ray
 */
public class ClientVersion extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        JSONObject object = new JSONObject();
        object.put("avalon", "v" + ConstantPool.Version.avalon);
	    object.put("servlet", "v" + ConstantPool.Version.instance().servlet());
	    resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(object.toString());
    }
}
