package avalon.servlet.manager;

import avalon.main.MainServer;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by Eldath Ray on 2017/4/18 0018.
 * <p>
 * Usage: GET .../avalon/v0/manager/manage_instance?action={start/stop/restart}&target={servlet/wechat/all}
 *
 * @author Eldath Ray
 */
public class InstanceManager extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String target = req.getParameter("target");
        JSONObject object = new JSONObject();
        if ((action.equals("start") || action.equals("stop")) || (target.equals("servlet") ||
                target.equals("wechat") || target.equals("Avalon") || target.equals("All"))) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            object.put("message", "调用格式不正确。");
            resp.getWriter().print(object.toString());
            return;
        }
        try {
            switch (action) {
                case "stop":
                    stop(target, resp);
                    break;
                case "start":
                    start(target, resp);
                    break;
                case "restart":
                    restart(target, resp);
                    break;
            }
        } catch (Exception e) {
            object.put("message", "处理请求时发生异常：" + e.toString());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void stop(String target, HttpServletResponse resp) throws IOException {
        switch (target) {
            case "servlet":
                if (MainServer.getWebqqProcess().isAlive()) MainServer.getWebqqProcess().destroy();
                else handleStopError(resp);
                break;
            case "wechat":
                if (MainServer.getWechatProcess().isAlive()) MainServer.getWechatProcess().destroy();
                else handleStopError(resp);
                break;
            case "all":
                stop("servlet", resp);
                stop("wechat", resp);
                break;
        }
    }

    private void start(String target, HttpServletResponse resp) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String commandPrefix = "perl " + System.getProperty("user.dir") + File.separator + "bin" + File.separator;
        switch (target) {
            case "servlet":
                if (MainServer.getWebqqProcess() == null || !MainServer.getWebqqProcess().isAlive())
                    MainServer.setWebqqProcess(runtime.exec(commandPrefix + "Mojo-Webqq.pl"));
                else handleStartError(resp);
                break;
            case "wechat":
                if (MainServer.getWechatProcess() == null || !MainServer.getWechatProcess().isAlive())
                    MainServer.setWechatProcess(runtime.exec(commandPrefix + "Mojo-Weixin.pl"));
                else handleStartError(resp);
                break;
            case "All": {
                start("servlet", resp);
                start("wechat", resp);
                break;
            }
        }
    }

    private void restart(String target, HttpServletResponse resp) throws IOException {
        stop(target, resp);
        start(target, resp);
    }

    private void handleStopError(HttpServletResponse response) throws IOException {
        handleError(response, "无法终止进程：已被终止或未知错误。");
    }

    private void handleStartError(HttpServletResponse response) throws IOException {
        handleError(response, "无法启动进程：已经启动或未知错误。");
    }

    private void handleError(HttpServletResponse response, String message) throws IOException {
        JSONObject object = new JSONObject();
        object.put("message", message);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print(object.toString());
    }
}
