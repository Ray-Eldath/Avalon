package avalon.tool;

import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static avalon.tool.pool.ConstantPool.Address.wechat;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Responder {
    private static final Logger logger = Logger.getGlobal();
    private static final List<String> replaceList = new ArrayList<>();

    static {
        replaceList.add("小怪冰");
        replaceList.add("小冰冰");
        replaceList.add("冰酱");
        replaceList.add("小冰");
    }

    private Responder() {
    }

    public static String responseXiaoIce(String content) {
        try {
            JSONTokener tokener = new JSONTokener(new URL(wechat + "/openwx/consult?account=xiaoice-ms&content=" +
                    UrlEncoded.encodeString(content)).openStream());
            JSONObject object = (JSONObject) tokener.nextValue();
            if (object.isNull("reply")) return null;
            String reply = new String(object.get("reply").toString().getBytes(), "UTF-8");
            if (reply.contains("[语音]")) return null;
            else if (reply.contains("[图片]")) return null;
            for (String thisReplaceWord : replaceList)
                reply = reply.replace(thisReplaceWord, "Avalon");
            return reply;
        } catch (IOException e) {
            logger.warning("IOException thrown while responseXiaoIce: " + e);
            return null;
        }
    }
}
