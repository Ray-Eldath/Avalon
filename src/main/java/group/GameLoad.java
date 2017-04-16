package group;

import tool.GameScriptLoader;
import util.GScript;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/3/25 0025.
 *
 * @author Eldath Ray
 */
public class GameLoad extends BaseGroupMessageResponder {
    private static GameLoad ourInstance = new GameLoad();

    public static GameLoad getInstance() {
        return ourInstance;
    }

    private GameLoad() {
    }

    @Override
    public void doPost(GroupMessage message) {
        int id = Integer.parseInt(message.getContent().replace("avalon game load ", ""));
        System.out.println("id=" + id);
        GScript script = GameScriptLoader.getInstance().getScript(id);
        if (script == null) {
            message.response("未找到ID为" + id + "的剧本！请执行\"avalon game info\"以查看剧本信息！(⊙ᗜ⊙)");
            return;
        }
        boolean status = GameScriptLoader.getInstance().load(script);
        if (status) message.response("剧本" + script.getId() + "：" + script.getName() +
                "装载成功！游戏模式已经启动！注意：游戏模式下将只有游戏指令响应器工作！");
        else message.response("剧本" + id + "装载失败：未知错误`(*>﹏<*)′");
    }

    @Override
    public String getHelpMessage() {
        return "avalon game load <剧本ID>：加载指定剧本";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon game load");
    }
}
