package avalon.group;

import avalon.api.util.GroupMessage;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/25 0025.
 *
 * @author Eldath Ray
 */
public class Shutdown extends BaseGroupMessageResponder {
    private static Shutdown instance = null;

    public static Shutdown getInstance() {
        if (instance == null) instance = new Shutdown();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        long[] admins = MainGroupMessageHandler.getAdminUid();
        for (long admin : admins)
            if (admin == message.getSenderUid()) {
                System.exit(0);
                LoggerFactory.getLogger(Shutdown.class).warn("Avalon is stopped remotely by " +
                        message.getSenderUid() + " : " + message.getSenderNickName() + " on " +
                        message.getGroupUid() + " : " + message.getGroupName() + " at " +
                        message.getTime().toString().replace("T", " "));
            }
    }

    @Override
    public String getHelpMessage() {
        return "avalon shutdown|exit：<管理员> 退出Avalon。";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon shutdown|avalon exit");
    }
}
