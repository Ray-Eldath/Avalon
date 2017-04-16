package group;

import data.ConfigSystem;
import main.MainServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.APISurvivePool;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class ResponderManager extends BaseGroupMessageResponder {
    private static ResponderManager instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ResponderManager.class);
    private static final long[] restartAllowUid = ConfigSystem.getInstance()
            .getCommandAllowArray("ResponderManager_restart");
    private static final long[] allowPeople = ConfigSystem.getInstance()
            .getCommandAllowArray("ResponderManager_basic");
    private static final long[] stopAllowUid = ConfigSystem.getInstance()
            .getCommandAllowArray("ResponderManager_stop");
    private static final BaseGroupMessageResponder[] canNotBanAPI =
            new BaseGroupMessageResponder[]{Shutdown.getInstance()};


    public static ResponderManager getInstance() {
        if (instance == null) instance = new ResponderManager();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String content = message.getContent();
        String sender = message.getSenderNickName();
        long senderUid = message.getSenderUid();
        String action, apiName;
        for (long thisFollowFriend : allowPeople)
            if (senderUid == thisFollowFriend) {
                if (!content.contains(" ")) {
                    message.response("@" + sender +
                            " 您的指示格式不对辣！（｀Δ´）！请注意在API触发语句后是否缺少空格~");
                    return;
                }
                apiName = content.toLowerCase().
                        replace("avalon respondermanager stop ", "").replace("avalon respondermanager start ", "");
                BaseGroupMessageResponder thisAPI = MainServlet.getAPIByKeyword(apiName);
                action = content.toLowerCase().
                        replace("avalon respondermanager ", "").replace(apiName, "").trim();
                if (thisAPI == null) {
                    message.response("@" + sender + " 您要操作的指令响应器根本不存在！(╯︵╰,)");
                    return;
                }
                for (BaseGroupMessageResponder thisCanNotBanRunner : canNotBanAPI)
                    if (thisAPI.equals(thisCanNotBanRunner)) {
                        message.response("@" + sender + "您要操作的指令响应器可不能被禁止啊！(´Д` )");
                        return;
                    }
                if ("start".equals(action)) {
                    for (long thisAllowStartUid : restartAllowUid)
                        if (thisAllowStartUid == senderUid) {
                            APISurvivePool.getInstance().setAPISurvive(thisAPI, true);
                            message.response("@" + sender + " 您要重启的指令响应器将会重启`(*∩_∩*)′");
                            logger.info("BaseGroupMessageResponder " + thisAPI.getClass().getName() + " is reopened by " +
                                    senderUid + " : " + sender + ".");
                            return;
                        }
                }
                if ("stop".equals(action)) {
                    for (long thisStopAllowUid : stopAllowUid) {
                        if (thisStopAllowUid == senderUid) {
                            APISurvivePool.getInstance().setAPISurvive(thisAPI, false);
                            message.response("@" + sender + " 您要关闭的指令响应器将会关闭~=-=");
                            logger.info("BaseGroupMessageResponder " + thisAPI.getClass().getName() + " is closed by " +
                                    senderUid + " : " + sender + ".");
                            return;
                        }
                    }
                } else {
                    message.response("@" + sender + " 您的指示格式不对辣！（｀Δ´）！");
                    return;
                }
            }
        message.response("@" + sender + " 您没有权限啦！(゜д゜)");
    }

    @Override
    public String getHelpMessage() {
        return "avalon respondermanager (start/stop)：<管理员> 控制指令响应器开/关";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon respondermanager ");
    }
}
