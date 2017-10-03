package avalon.friend;

import avalon.util.BasicResponder;
import avalon.util.FriendMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/28.
 *
 * @author Eldath Ray
 */
public abstract class FriendMessageResponder implements BasicResponder {
	public abstract void doPost(FriendMessage message);

	public abstract String getHelpMessage();

	public abstract Pattern getKeyWordRegex();
}
