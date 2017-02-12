package api;

import tool.Response;
import util.GroupMessage;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Help implements GroupMessageAPI {
    private static Help instance = null;

    static Help getInstance() {
        if (instance == null) instance = new Help();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        // CUSTOM 若更改了触发关键词，请在此修改。
        Response.responseGroup(message.getGroupUid(), "This is Avalon. 以下是我的帮助资料：\n" +
                "<关键词>：<触发的作用效果>，所有关键词均忽略大小写并且以avalon开头\n" +
                "avalon version：查看版本信息\n" +
                "膜*关键词：随机触发膜*语句\n" +
                "avalon answer/tell me 或 阿瓦隆回答/告诉我)+任意语句：将语句转发至小冰并显示回复\n" +
                "avalon help：显示本内容\n" +
                "avalon echo + 指定语句：使阿瓦隆说您指定的话\n" +
                "avalon APIManager (start/stop)：控制插件开/关，需要特定权限\n" +
                "avalon blacklist add/remove + 需屏蔽的ID：使某账号不能调用小冰，需要特定权限"); //+
        // "\n（我才不会告诉你我有一些没有写在这里的彩蛋指令呢~哈哈`(*∩_∩*)′）");
        // TODO v0.0.1正式版本记得写彩蛋
    }
}
