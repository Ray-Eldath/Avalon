package test;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) throws Exception {
        JSONObject object = (JSONObject) new JSONTokener(
                new URL("http://127.0.0.1:5000/openqq/get_client_info").openStream()).nextValue();
        System.out.println(object.getString("version"));
    }
}
