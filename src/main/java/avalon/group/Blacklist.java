package avalon.group;

import avalon.tool.system.ConfigSystem;
import avalon.util.GroupMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

import static avalon.tool.Responder.AT;

/**
 * Created by Eldath on 2017/1/30 0030.
 *
 * @author Eldath
 */
public class Blacklist extends BaseGroupMessageResponder {
    private static Logger logger = LoggerFactory.getLogger(Blacklist.class);
    private static final long[] allowList = ConfigSystem.getInstance().getCommandAllowArray("Blacklist_basic");
    private static final Blacklist instance = new Blacklist();

    public static Blacklist getInstance() {
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        final long sender_uid = message.getSenderUid();
        final int max = MainGroupMessageHandler.getPunishFrequency();
        final String content = message.getContent();
        final String sender = message.getSenderNickName();
        String[] split;
        String action;
        long toBan;
        if (!content.contains(" ")) {
            message.response(AT(message) + " 您的指示格式不对辣！（｀Δ´）！");
            return;
        }
        split = content.split(" ");
        if (split.length < 3) {
            message.response(AT(message) + " 您的指示格式不对辣！（｀Δ´）！");
            return;
        }
        action = split[2];
        toBan = Long.parseLong(split[3]);
        for (long thisAllowUid : allowList)
            if (thisAllowUid == sender_uid) {
                if ("add".equals(action)) {
                    message.response(AT(message) + " 帐号" + toBan + "现已被屏蔽。");
                    logger.info("Account " + toBan + " is baned by " + sender_uid + " : " + sender + ".");
                    MainGroupMessageHandler.getSetBlackListPeopleMap().put(toBan, max);
                    return;
                } else if ("remove".equals(action)) {
                    if (!MainGroupMessageHandler.getSetBlackListPeopleMap().containsKey(toBan)) {
                        message.response(AT(message) + " 好像帐号" + toBan + "没有被屏蔽过呢-。-");
                        return;
                    }
                    message.response(AT(message) + " 帐号" + toBan + "的屏蔽已被解除(^.^)");
                    logger.info("Account " + toBan + " is allowed again by " + sender_uid + " : " + sender + ".");
                    MainGroupMessageHandler.getSetBlackListPeopleMap().put(toBan, 0);
                    return;
                } else {
                    message.response(AT(message) + " 您的指示格式不对辣！（｀Δ´）！");
                    return;
                }
            }
        message.response(AT(message) + " 您没有权限辣！（｀Δ´）！");
    }

    @Override
    public String getHelpMessage() {
        return "avalon blacklist (add|remove)：<管理员> 将指定的QQ号 添加至黑名单或从黑名单移除";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon blacklist add |avalon blacklist remove ");
    }
}
