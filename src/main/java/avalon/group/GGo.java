package avalon.group;

import avalon.data.GameData;
import avalon.data.GameRunningData;
import avalon.extend.BaseGameResponder;
import avalon.tool.DelayResponse;
import avalon.util.*;

import java.util.regex.Pattern;

import static avalon.util.GPathDirections.fromName;

/**
 * Created by Eldath Ray on 2017/3/24.
 *
 * @author Eldath Ray
 */
public class GGo extends BaseGameResponder {
    private static GGo instance = null;

    public static GGo getInstance() {
        if (instance == null) instance = new GGo();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String direction = message.getContent().replace("go ", "").toLowerCase();
        GPathDirection pathDirection = fromName(direction);
        String nickname = message.getSenderNickName();
        long uid = message.getSenderUid();
        final GPlayer player = GameRunningData.GroupAndPlayer.getPlayer(message.getSenderUid());
        if (player == null) {
            message.response("账户" + uid + "未注册！请先执行\"game register <玩家昵称>\"以注册您的账户！");
            return;
        }
        final GRoom playerLocation = player.getLocation();
        GPath path = GameData.Map.getGameMap().filter((gPath -> gPath.getFromRoom().equals(playerLocation) &&
                gPath.getExitDirection().equals(pathDirection))).findFirst().orElse(null);
        GRoom toRoom;
        if (path == null) {
            message.response("@" + nickname + "不存在" + playerLocation.getName() + "向" +
                    pathDirection.getName() + "的出口哇！(⊙o⊙)");
        } else {
            toRoom = path.getToRoom();
            message.response("@" + nickname + player.getName() + "正前往" + toRoom.getName() + "... ...");
            DelayResponse.getInstance().delay(new DelayMessage(path.getMoveDelaySecond(), message, () -> {
                player.setLocation(toRoom);
                return "@" + nickname + player.getName() + "已到达" + toRoom.getName();
            }));
        }
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("go \\w{1,5}");
    }

    @Override
    public String getGameHelpMessage() {
        return null;
    }
}
