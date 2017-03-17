package command;

import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/3/11 0011.
 *
 * @author Eldath Ray
 */
public class TestGroup extends GroupMessageAPI {
    private static TestGroup instance = null;

    public static TestGroup getInstance() {
        if (instance == null) instance = new TestGroup();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        System.out.println("actived by " + message.getSenderNickName());
        message.response("xxx");
    }

    @Override
    public String getHelpMessage() {
        return "";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon test");
    }
}
