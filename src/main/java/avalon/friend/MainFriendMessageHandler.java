package avalon.friend;

import avalon.extend.Recorder;
import avalon.util.FriendMessage;

/**
 * Created by Eldath Ray on 2017/4/1 0001.
 *
 * @author Eldath Ray
 */
public class MainFriendMessageHandler {
    private static MainFriendMessageHandler ourInstance = new MainFriendMessageHandler();

    public static MainFriendMessageHandler getInstance() {
        return ourInstance;
    }

    public void handle(FriendMessage message) {
//        if (MessageChecker.checkEncode(message)) return;
        Recorder.getInstance().recodeFriendMessage(message);
    }
}
