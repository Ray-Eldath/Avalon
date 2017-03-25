package command;

import tool.GameScriptLoader;
import tool.VariablePool;
import util.GroupMessage;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/25 0025.
 *
 * @author Eldath Ray
 */
public class GGameInfo extends BaseGroupMessageCommandRunner {
    private static GGameInfo ourInstance = new GGameInfo();

    public static GGameInfo getInstance() {
        return ourInstance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String response = "";
        List<String> strings = GameScriptLoader.getInstance().getScriptMessages();
        for (String thisString : strings)
            response += "\n" + thisString;
        message.response("剧本总数：" + GameScriptLoader.getInstance().getScripts().count() + "\t已加载："
                + VariablePool.GameMode.Is_Loaded + response);
    }

    @Override
    public String getHelpMessage() {
        return "avalon game info：列出所有剧本信息";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon game info");
    }
}
