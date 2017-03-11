package main;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Eldath on 2017/3/11 0011.
 *
 * @author Eldath Ray
 */
public class MainPostMonitor extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject object = (JSONObject) new JSONTokener(req.getInputStream()).nextValue();
        System.out.println(object);
    }
}
