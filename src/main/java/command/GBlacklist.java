package command;

import data.ConfigSystem;
import main.MainServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/30 0030.
 *
 * @author Eldath
 */
@SuppressWarnings("ALL")
public class GBlacklist extends BaseGroupMessageCommandRunner {
    private static Logger logger = LoggerFactory.getLogger(GBlacklist.class);
    private static final long[] allowList = ConfigSystem.getInstance()
            .getCommandAllowArray("Blacklist_basic");
    private static GBlacklist instance = null;

    public static GBlacklist getInstance() {
        if (instance == null) instance = new GBlacklist();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        final long sender_uid = message.getSenderUid();
        final long group_uid = message.getGroupUid();
        final int max = MainServlet.punishFrequency;
        final String content = message.getContent();
        final String sender = message.getSenderNickName();
        String[] split;
        String action;
        long toBan;
        if (!content.contains(" ")) {
            message.response("@\u2005" + sender + " 您的指示格式不对辣！（｀Δ´）！");
            return;
        }
        split = content.split(" ");
        if (split.length < 3) {
            message.response("@\u2005" + sender + " 您的指示格式不对辣！（｀Δ´）！");
            return;
        }
        action = split[2];
        toBan = Long.parseLong(split[3]);
        for (long thisAllowUid : allowList)
            if (thisAllowUid == sender_uid) {
                if ("add".equals(action)) {
                    message.response("@\u2005" + sender + " 帐号" + toBan + "现已被屏蔽。");
                    logger.info("Account " + toBan + " is baned by " + sender_uid + " : " + sender + ".");
                    MainServlet.blackListPeopleMap.put(toBan, max);
                    return;
                } else if ("remove".equals(action)) {
                    if (!MainServlet.blackListPeopleMap.containsKey(toBan)) {
                        message.response("@\u2005" + sender + " 好像帐号" + toBan + "没有被屏蔽过呢-。-");
                        return;
                    }
                    message.response("@\u2005" + sender + " 帐号" + toBan + "的屏蔽已被解除(^.^)");
                    logger.info("Account " + toBan + " is allowed again by " + sender_uid + " : " + sender + ".");
                    MainServlet.blackListPeopleMap.put(toBan, 0);
                    return;
                } else {
                    message.response("@\u2005" + sender + " 您的指示格式不对辣！（｀Δ´）！");
                    return;
                }
            }
        message.response("@\u2005" + sender + " 您没有权限辣！（｀Δ´）！");
    }

    @Override
    public String getHelpMessage() {
        return "avalon blacklist (add/remove)：将指定的QQ号 添加至黑名单/从黑名单移除";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon blacklist add |avalon blacklist remove ");
    }
}
