package extend;

import tool.VariablePool;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/24.
 *
 * @author Eldath Ray
 */
public class GGoto extends GameMessage {
    @Override
    public void doPost(GroupMessage message) {
        if (!VariablePool.Is_Game_Mode_On) return;
        String content = message.getContent();
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("go|goto");
    }

    @Override
    public String getGameHelpMessage() {
        return null;
    }
}
