package extend;

import data.GameRunningData;
import data.GameRunningData.GroupAndPlayer;
import util.GGroup;
import util.GPacket;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/24.
 *
 * @author Eldath Ray
 */
public class GNewGroup extends BaseGameCommandRunner {
    private static GNewGroup instance = null;

    public static GNewGroup getInstance() {
        if (instance == null) instance = new GNewGroup();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        final String content = message.getContent();
        final String groupName = content.replace("game group new ", "");
        if (GameRunningData.GroupAndPlayer.isGroupExist(groupName)) {
            message.response("指定的小组" + groupName + "已存在！");
            return;
        }
        final int id = GroupAndPlayer.getTotalGroup() + 1;
        final GGroup group = new GGroup(id, groupName, new GPacket());
        final boolean status = GroupAndPlayer.newGroup(group);
        if (status) message.response("指定的小组" + groupName + "新建成功！\\(^o^)/\n请记住您的小组ID：" + id);
        else message.response("小组" + groupName + "新建失败：未知错误(＝。＝)");
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("game group new (\\w|[\\u4e00-\\u9fa5])+");
    }

    @Override
    public String getGameHelpMessage() {
        return "game group new <小组名>：新建小组。";
    }
}