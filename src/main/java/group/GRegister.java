package group;

import data.GameRunningData;
import extend.BaseGameResponder;
import util.GPlayer;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/24.
 *
 * @author Eldath Ray
 */
public class GRegister extends BaseGameResponder {
    private static GRegister instance = null;

    public static GRegister getInstance() {
        if (instance == null) instance = new GRegister();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String name = message.getContent().replace("game register ", "");
        long id = message.getSenderUid();
        if (GameRunningData.GroupAndPlayer.isPlayerExist(id)) {
            message.response("QQ号为" + id + "的玩家已经存在！您可以直接加入小组以开始游戏！\\(^o^)/~");
            return;
        } else if (GameRunningData.GroupAndPlayer.isPlayerExist(name)) {
            message.response("昵称为" + name + "的玩家已经存在！(。・_・)/~~~");
            return;
        }
        GPlayer player = new GPlayer(id, name);
        boolean status = GameRunningData.GroupAndPlayer.newPlayer(player);
        if (status) message.response("玩家" + name + "注册成功。您现在可以使用" +
                "\"game group join <小组ID/名称>\"来加入小组！￣O￣)ノ");
        else message.response("玩家" + name + "新建失败：未知错误╮(╯_╰)╭");
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("game register (\\w|[\\u4e00-\\u9fa5])+");
    }

    @Override
    public String getGameHelpMessage() {
        return "game register <玩家昵称>：注册发送消息的QQ账户为指定昵称的玩家";
    }
}
