package avalon.group;

import avalon.tool.ConfigSystem;
import avalon.util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Echo extends BaseGroupMessageResponder {
    private static Echo instance = null;
    private static final long[] allowList = ConfigSystem.getInstance().getCommandAllowArray("Echo_basic");

    public static Echo getInstance() {
        if (instance == null) instance = new Echo();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String content = message.getContent();
        long sender_uid = message.getSenderUid();
        String sender = message.getSenderNickName();
        String[] split = content.split(" ");
        if (split.length < 1) {
            message.response("您的指示恕我不能遵守⊙﹏⊙! 因为不合规范嘛(╯︵╰,)");
            return;
        }
        for (long thisAllow : allowList)
            if (sender_uid == thisAllow) {
                message.response(split[2]);
                return;
            }
        message.response("@" + sender + " 您没有权限欸... ...(゜д゜)");
    }

    @Override
    public String getHelpMessage() {
        return "avalon echo | avalon repeat | 阿瓦隆跟我说：<管理员> 让阿瓦隆重复给定语句";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon echo |avalon repeat |阿瓦隆跟我说 ");
    }
}
