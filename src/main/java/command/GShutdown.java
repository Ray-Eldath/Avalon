package command;

import main.MainServlet;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/25 0025.
 *
 * @author Eldath Ray
 */
public class GShutdown extends BaseGroupMessageCommandRunner {
    private static GShutdown instance = null;

    public static GShutdown getInstance() {
        if (instance == null) instance = new GShutdown();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        for (long admin : MainServlet.adminUid)
            if (admin == message.getSenderUid())
                System.exit(0);
    }

    @Override
    public String getHelpMessage() {
        return "avalon shutdown/exit：退出Avalon。";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon shutdown|avalon exit");
    }
}
