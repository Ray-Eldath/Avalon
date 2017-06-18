package avalon.group;

import avalon.util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Echo extends BaseGroupMessageResponder {
    private static Echo instance = null;

    public static Echo getInstance() {
        if (instance == null) instance = new Echo();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String content = message.getContent().replace("avalon echo ", "")
                .replace("avalon repeat ", "")
                .replace("阿瓦隆跟我说 ", "");
        message.response(content);
    }

    @Override
    public String getHelpMessage() {
        return "avalon echo | avalon repeat | 阿瓦隆跟我说：让阿瓦隆重复给定语句";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon echo |avalon repeat |阿瓦隆跟我说 ");
    }
}
