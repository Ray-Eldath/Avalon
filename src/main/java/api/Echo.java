package api;

import tool.Response;
import util.GroupMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Echo implements GroupMessageAPI {
    private static Echo instance = null;
    private static List<Long> allowList = new ArrayList<>();

    static {
        // CUSTOM 以下为允许 使Avalon重复说指定语句 的QQ号
        allowList.add(1464443139L);
    }

    static Echo getInstance() {
        if (instance == null) instance = new Echo();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String content = message.getContent();
        long sender_uid = message.getSenderUid();
        long group_uid = message.getGroupUid();
        String sender = message.getSenderNickName();
        if (!content.contains(" ")) {
            Response.responseGroup(group_uid, "您的指示恕我不能遵守⊙﹏⊙! 因为不合规范嘛(╯︵╰,)");
            return;
        }
        for (long thisAllow : allowList)
            if (sender_uid == thisAllow) {
                Response.responseGroup(group_uid, content.split(" ")[2]);
                return;
            }
        Response.responseGroup(group_uid, "@" + sender + " 您没有权限欸... ...(゜д゜)");
    }
}
