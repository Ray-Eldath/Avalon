package api;

import org.json.JSONObject;
import tool.Response;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Version implements API {
    private static Version instance = null;

    static Version getInstance() {
        if (instance == null) instance = new Version();
        return instance;
    }

    @Override
    public void doPost(JSONObject object) {
        String group_uid = object.get("group_uid").toString();
        try {
            String message = "Hi, I'm Avalon.\n" +
                    "我是阿瓦隆，QQ群机器人。\n" +
                    "我的名字和头像均取自《Implosion》，我的父亲是Mojo-Webqq。\n" +
                    "我由Eldath Ray进行二次开发。\n" +
                    "我在GitHub上开源，欢迎访问我的仓库：\n" +
                    "Mojo-Webqq Version: v2.0.4\tMojo-Weixin Version: v1.2.9\tAvalon Version: v0.0.1 Pre-Alpha";
            // 本人不希望开发者标识被去除。谢谢合作。
            Response.responseGroup(group_uid, message);
        } catch (Exception ignore) {
        }
    }
}
