package extend;

import data.GameRunningData;
import util.GGroup;
import util.GPlayer;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/24.
 *
 * @author Eldath Ray
 */
public class GJoinGroup extends BaseGameResponder {
    private static GJoinGroup instance = null;

    public static GJoinGroup getInstance() {
        if (instance == null) instance = new GJoinGroup();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String content = message.getContent();
        if (Pattern.matches("game group join \\d+", content)) {
            int id = Integer.parseInt(content.replace("game group join ", ""));
            GGroup group = GameRunningData.GroupAndPlayer.getGroup(id);
            if (group == null)
                message.response("指定的小组ID" + id + "不存在！╮(╯_╰)╭");
            else
                checkAccount(group, message);
        } else if (Pattern.matches("game group join (\\w|[\\u4e00-\\u9fa5])+", content)) {
            String groupName = content.replace("game group join ", "");
            GGroup group = GameRunningData.GroupAndPlayer.getGroup(groupName);
            if (group == null)
                message.response("指定的小组名" + groupName + "不存在！(。・_・)/");
            else checkAccount(group, message);
        }
    }

    private static void checkAccount(GGroup group, GroupMessage message) {
        long uid = message.getSenderUid();
        GPlayer player = GameRunningData.GroupAndPlayer.getPlayer(message.getSenderUid());
        if (player == null) {
            message.response("账户" + uid + "未注册！请先执行\"game register <玩家昵称>\"以注册您的账户！");
        } else if (GameRunningData.GroupAndPlayer.joinGroup(group, player)) {
            message.response("账户为" + uid + "的玩家成功加入小组" + group.getName() +
                    "。您现在可以开始游戏啦~\\(≧▽≦)/~");
        } else {
            message.response("账户" + message.getSenderNickName() + "无法加入小组" + group.getName() + "：未知错误(⊙﹏⊙)");
        }
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("game group join (\\d|\\w|[\\u4e00-\\u9fa5])+");
    }

    @Override
    public String getGameHelpMessage() {
        return "game group join <小组ID/小组名>：加入小组";
    }
}
