package avalon.friend;

import avalon.util.FriendMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/28.
 *
 * @author Eldath Ray
 */
public abstract class BaseFriendMessageRunner {
    public abstract void doPost(FriendMessage message);

    public abstract String getHelpMessage();

    public abstract Pattern getKeyWordRegex();
}
