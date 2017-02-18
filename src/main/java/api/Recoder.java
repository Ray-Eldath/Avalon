package api;

import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class Recoder extends GroupMessageAPI {
    @Override
    public void doPost(GroupMessage message) {
    }

    @Override
    public String getHelpMessage() {
        return null;
    }

    @Override
    public Pattern getKeyWordRegex() {
        return null;
    }
}
