package test;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class GetTest {
    public static void main(String[] args) throws Exception {
        // 目前开发者已经解决问题。这个备用方法就... ...还是要完成得。不过目前暂缓。
	    StringBuilder cookie = new StringBuilder();
        BufferedReader reader = Files.newBufferedReader(Paths.get(System.getProperty("java.io.tmpdir") +
                File.separator + "mojo_webqq_cookie_default.dat"), Charset.forName("GBK"));
        String thisLine;
        while ((thisLine = reader.readLine()) != null)
	        cookie.append(thisLine).append("\n");
        System.out.println(cookie);
        System.exit(0);
        URLConnection connection = new URL("").openConnection();
	    connection.addRequestProperty("Cookie", cookie.toString());
        JSONObject object = (JSONObject)
                new JSONTokener(new URL("https://graph.qq.com/oauth2.0/authorize").openStream()).nextValue();
        System.out.println(object.toString());
    }
}
