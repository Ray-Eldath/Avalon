package api;

import org.json.JSONObject;
import tool.Response;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Help implements API {
    private static Help instance = null;

    public static Help getInstance() {
        if (instance == null) instance = new Help();
        return instance;
    }

    @Override
    public void doPost(JSONObject object) {
        Response.responseGroup(object.get("group_uid").toString(), "This is Avalon. 以下是我的帮助资料：\n" +
                "<关键词>：<触发的作用效果>\n" +
                "avalon version：查看版本信息\n" +
                "膜*关键词：随机触发膜*语句\n" +
                "avalon answer me+任意语句 或 阿瓦隆回答我：将语句转发至小冰并显示回复\n" +
                "avalon help：显示本内容\n" +
                "avalon APIManager (start/stop)：控制插件开/关，需要特定权限");
    }

    @Override
    public void response(String groupNumber) {

    }
}
