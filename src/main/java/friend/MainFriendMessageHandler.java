package friend;

import extend.Recorder;
import util.FriendMessage;

/**
 * Created by Eldath Ray on 2017/3/30.
 *
 * @author Eldath Ray
 */
public class MainFriendMessageHandler {
    public static void handle(FriendMessage message) {
        Recorder.getInstance().recodeFriendMessage(message);
    }
}
