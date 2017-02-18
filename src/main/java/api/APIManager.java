package api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.APISurvivePool;
import tool.Response;
import util.GroupMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APIManager extends GroupMessageAPI {
    private static APIManager instance = null;
    private static Logger logger = LoggerFactory.getLogger(APIManager.class);
    private static List<Long> stopAllowUid = new ArrayList<>();
    private static List<Long> startAllowUid = new ArrayList<>();

    static {
        // CUSTOM 以下为允许关闭API的QQ号
        stopAllowUid.add(1464443139L);
        stopAllowUid.add(360736041L);
        stopAllowUid.add(704639565L);
        stopAllowUid.add(951394653L);
        stopAllowUid.add(1016281105L);
        // CUSTOM 以下为允许重启API的QQ号
        startAllowUid.add(1464443139L);
        startAllowUid.add(704639565L);
        startAllowUid.add(951394653L);
    }

    static APIManager getInstance() {
        if (instance == null) instance = new APIManager();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String content = message.getContent();
        String sender = message.getSenderNickName();
        long senderUid = message.getSenderUid();
        long groupUid = message.getGroupUid();
        String action, apiName;
        for (long thisFollowFriend : MainServlet.followPeople)
            if (senderUid == thisFollowFriend) {
                if (!content.contains(" ")) {
                    Response.responseGroup(groupUid, "@" +
                            sender + " 您的指示格式不对辣！（｀Δ´）！请注意在API触发语句后是否缺少空格~");
                    return;
                }
                apiName = content.toLowerCase().
                        replace("avalon apimanager stop ", "").replace("avalon apimanager start ", "");
                GroupMessageAPI thisAPI = MainServlet.getAPIByKeyword(apiName);
                action = content.toLowerCase().
                        replace("avalon apimanager ", "").replace(apiName, "").trim();
                if (thisAPI == null) {
                    Response.responseGroup(groupUid, "@" + sender + " 您要操作的API根本不存在！(╯︵╰,)");
                    return;
                }
                if ("start".equals(action)) {
                    for (long thisAllowStartUid : startAllowUid)
                        if (thisAllowStartUid == senderUid) {
                            APISurvivePool.getInstance().setAPISurvive(thisAPI, true);
                            Response.responseGroup(groupUid, "@" + sender + " 您要重启的API将会重启`(*∩_∩*)′");
                            logger.info("GroupMessageAPI " + thisAPI.getClass().getName() + " is reopened by " +
                                    senderUid + " : " + sender + ".");
                            return;
                        }
                }
                if ("stop".equals(action)) {
                    for (long thisStopAllowUid : stopAllowUid) {
                        if (thisStopAllowUid == senderUid) {
                            APISurvivePool.getInstance().setAPISurvive(thisAPI, false);
                            Response.responseGroup(groupUid, "@" + sender + " 您要关闭的API将会关闭~");
                            logger.info("GroupMessageAPI " + thisAPI.getClass().getName() + " is closed by " +
                                    senderUid + " : " + sender + ".");
                            return;
                        }
                    }
                } else {
                    Response.responseGroup(groupUid, "@" + sender + " 您的指示格式不对辣！（｀Δ´）！");
                    return;
                }
            }
        Response.responseGroup(groupUid, "@" + sender + " 您没有权限啦！(゜д゜)");
    }

    @Override
    public String getHelpMessage() {
        return "avalon APIManager (start/stop)：控制插件开/关，需要特定权限";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon apimanager ");
    }
}
