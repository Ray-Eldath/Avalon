package avalon.group;

import avalon.tool.pool.ConstantPool;
import avalon.util.GroupMessage;

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
        // FIXME 不知道为啥，这么写就是不行。理论上应该是可以的。
//        Map<Pattern, BaseGroupMessageResponder> apiList = MainGroupMessageHandler.getInstance().getApiList();
//        StringBuilder messageShow = new StringBuilder();
//        for (BaseGroupMessageResponder api : apiList.values()) {
//            String helpMessage = api.getHelpMessage();
//            if (helpMessage == null || "".equals(helpMessage)) continue;
//            messageShow.append("\n").append(api.getHelpMessage());
//        }
        String messageShow = AnswerMe.getInstance().getHelpMessage() + "\n" +
                Blacklist.getInstance().getHelpMessage() + "\n" +
                Echo.getInstance().getHelpMessage() + "\n" +
                Flush.getInstance().getHelpMessage() + "\n" +
                Help.getInstance().getHelpMessage() + "\n" +
                Mo.getInstance().getHelpMessage() + "\n" +
                Shutdown.getInstance().getHelpMessage() + "\n" +
                Version.getInstance().getHelpMessage() + "\n";
        message.response("This is Avalon. 以下是我的帮助资料：\n" +
                "<关键词>：<触发的作用效果>，所有关键词均忽略大小写并且以avalon开头\n" + messageShow + "For Avalon Version " +
                ConstantPool.Version.avalon);
        // "\n（我才不会告诉你我有一些没有写在这里的彩蛋指令呢~哈哈`(*∩_∩*)′）");
    }

    @Override
    public String getHelpMessage() {
        return "avalon help|帮助：显示本内容";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon help|avalon 帮助");
    }
}
