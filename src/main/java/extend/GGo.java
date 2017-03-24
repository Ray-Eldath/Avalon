package extend;

import data.GameRunningData;
import util.GPathDirection;
import util.GPathDirections;
import util.GPlayer;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/24.
 *
 * @author Eldath Ray
 */
public class GGo extends BaseGameCommandRunner {
    @Override
    public void doPost(GroupMessage message) {
        String direction = message.getContent().replace("go ", "").toLowerCase();
        GPathDirection pathDirection;
        switch (direction) {
            case "left":
                pathDirection = GPathDirections.LEFT;
                break;
            case "right":
                pathDirection = GPathDirections.RIGHT;
                break;
            case "up":
                pathDirection = GPathDirections.UP;
                break;
            case "down":
                pathDirection = GPathDirections.DOWN;
                break;
            case "front":
                pathDirection = GPathDirections.FRONT;
                break;
            case "back":
                pathDirection = GPathDirections.BACK;
                break;
            default:
                message.response("哪有您给的方向啊... ...方向只能是\"left\", \"right\", \"up\", " +
                        "\"down\", \"front\"或\"back\"噢~");
                return;
        }
        long uid = message.getSenderUid();
        GPlayer player = GameRunningData.GroupAndPlayer.getPlayer(message.getSenderUid());
        if (player == null) {
            message.response("账户" + uid + "未注册！请先执行\"game register <玩家昵称>\"以注册您的账户！");
            return;
        }
        // TODO GameData.Map.getGameMap().filter((gPath -> gPath.getFromRoom().equals()))
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
