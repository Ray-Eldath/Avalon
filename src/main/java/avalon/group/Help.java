package avalon.group;

import avalon.api.util.GroupMessage;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Help extends BaseGroupMessageResponder {
    private static Help instance = null;

    public static Help getInstance() {
        if (instance == null) instance = new Help();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        Map<Pattern, BaseGroupMessageResponder> apiList = MainGroupMessageHandler.getApiList();
        StringBuilder messageShow = new StringBuilder();
        for (BaseGroupMessageResponder api : apiList.values()) {
            String helpMessage = api.getHelpMessage();
            if (helpMessage == null || "".equals(helpMessage)) continue;
            messageShow.append("\n").append(api.getHelpMessage());
        }
        message.response("This is Avalon. 以下是我的帮助资料：\n" +
                "<关键词>：<触发的作用效果>，所有关键词均忽略大小写并且以avalon开头" + messageShow);
        // "\n（我才不会告诉你我有一些没有写在这里的彩蛋指令呢~哈哈`(*∩_∩*)′）");
        // TODO v0.0.1正式版本记得写彩蛋
    }

    @Override
    public String getHelpMessage() {
        return "avalon help：显示本内容";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon help|avalon 帮助");
    }
}
