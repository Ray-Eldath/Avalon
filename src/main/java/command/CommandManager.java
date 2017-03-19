package command;

import main.MainServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.APISurvivePool;
import tool.ConfigSystem;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class CommandManager extends GroupMessageCommand {
    private static CommandManager instance = null;
    private static Logger logger = LoggerFactory.getLogger(CommandManager.class);
    private static final long[] restartAllowUid = ConfigSystem.getInstance()
            .getCommandAllowArray("CommandManager_restart");
    private static final long[] allowPeople = ConfigSystem.getInstance()
            .getCommandAllowArray("CommandManager_basic");
    private static final long[] stopAllowUid = ConfigSystem.getInstance()
            .getCommandAllowArray("CommandManager_stop");


    public static CommandManager getInstance() {
        if (instance == null) instance = new CommandManager();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String content = message.getContent();
        String sender = message.getSenderNickName();
        long senderUid = message.getSenderUid();
        long groupUid = message.getGroupUid();
        String action, apiName;
        for (long thisFollowFriend : allowPeople)
            if (senderUid == thisFollowFriend) {
                if (!content.contains(" ")) {
                    message.response("@\u2005" + sender +
                            " 您的指示格式不对辣！（｀Δ´）！请注意在API触发语句后是否缺少空格~");
                    return;
                }
                apiName = content.toLowerCase().
                        replace("avalon commandmanager stop ", "").replace("avalon commandmanager start ", "");
                GroupMessageCommand thisAPI = MainServlet.getAPIByKeyword(apiName);
                action = content.toLowerCase().
                        replace("avalon commandmanager ", "").replace(apiName, "").trim();
                if (thisAPI == null) {
                    message.response("@\u2005" + sender + " 您要操作的指令响应器根本不存在！(╯︵╰,)");
                    return;
                }
                if ("start".equals(action)) {
                    for (long thisAllowStartUid : restartAllowUid)
                        if (thisAllowStartUid == senderUid) {
                            APISurvivePool.getInstance().setAPISurvive(thisAPI, true);
                            message.response("@\u2005" + sender + " 您要重启的指令响应器将会重启`(*∩_∩*)′");
                            logger.info("GroupMessageCommand " + thisAPI.getClass().getName() + " is reopened by " +
                                    senderUid + " : " + sender + ".");
                            return;
                        }
                }
                if ("stop".equals(action)) {
                    for (long thisStopAllowUid : stopAllowUid) {
                        if (thisStopAllowUid == senderUid) {
                            APISurvivePool.getInstance().setAPISurvive(thisAPI, false);
                            message.response("@\u2005" + sender + " 您要关闭的指令响应器将会关闭~");
                            logger.info("GroupMessageCommand " + thisAPI.getClass().getName() + " is closed by " +
                                    senderUid + " : " + sender + ".");
                            return;
                        }
                    }
                } else {
                    message.response("@\u2005" + sender + " 您的指示格式不对辣！（｀Δ´）！");
                    return;
                }
            }
        message.response("@\u2005" + sender + " 您没有权限啦！(゜д゜)");
    }

    @Override
    public String getHelpMessage() {
        return "avalon commandmanager (start/stop)：控制插件开/关，需要特定权限";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon commandmanager ");
    }
}
