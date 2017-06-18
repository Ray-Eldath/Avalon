package avalon.group;

import avalon.extend.Recorder;
import avalon.util.GroupMessage;

import java.util.regex.Pattern;

import static avalon.tool.Responder.AT;

/**
 * Created by Eldath Ray on 2017/3/26 0026.
 *
 * @author Eldath Ray
 */
public class Flush extends BaseGroupMessageResponder {
    private static Flush ourInstance = new Flush();

    public static Flush getInstance() {
        return ourInstance;
    }

    @Override
    public void doPost(GroupMessage message) {
        long senderUid = message.getSenderUid();
        long[] admins = MainGroupMessageHandler.getAdminUid();
        String sender = message.getSenderNickName();
        for (long thisAdmin : admins)
            if (senderUid == thisAdmin) {
                Recorder.getInstance().flushNow();
                System.gc();
                message.response("管理员：@" + sender + "缓存及临时文件刷新成功。");
                return;
            }
        message.response(AT(message) + "权限不足！");
    }

    @Override
    public String getHelpMessage() {
        return "avalon flush：<管理员> 刷新缓存并清除临时文件";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon flush");
    }
}
