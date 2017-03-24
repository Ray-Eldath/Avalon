package extend;

import command.BaseGroupMessageCommand;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/24.
 *
 * @author Eldath Ray
 */
public abstract class GameMessage extends BaseGroupMessageCommand {
    public abstract void doPost(GroupMessage message);

    public String getHelpMessage() {
        return "";
    }

    public abstract Pattern getKeyWordRegex();

    public abstract String getGameHelpMessage();
}
