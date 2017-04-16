package avalon.extend;

import avalon.data.GameRunningData;
import avalon.util.GPlayer;
import avalon.util.GroupMessage;

/**
 * Created by Eldath Ray on 2017/3/24.
 *
 * @author Eldath Ray
 */
public class BasicGetPlayer {
    private static BasicGetPlayer ourInstance = new BasicGetPlayer();

    public static BasicGetPlayer getInstance() {
        return ourInstance;
    }

    private BasicGetPlayer() {

    }

    private static GPlayer checkAccount(GroupMessage message) {
        long uid = message.getSenderUid();
        GPlayer player = GameRunningData.GroupAndPlayer.getPlayer(message.getSenderUid());
        if (player == null)
            message.response("账户" + uid + "未注册！请先执行\"game register <玩家昵称>\"以注册您的账户！");
        return player;
    }
}
