package avalon.group;

import avalon.util.GroupMessage;

import java.util.regex.Pattern;

import static avalon.tool.Responder.AT;
import static avalon.tool.pool.ConstantPool.Basic.currentServlet;

/**
 * Created by Eldath Ray on 2017/6/11 0011.
 *
 * @author Eldath Ray
 */
public class ShowAdmin extends BaseGroupMessageResponder {
    private static final ShowAdmin instance = new ShowAdmin();

    @Override
    public void doPost(GroupMessage message) {
        long[] adminUid = MainGroupMessageHandler.getAdminUid();
        StringBuilder builder = new StringBuilder(AT(message) + " 目前管理员有：\n");
        for (long uid : adminUid)
            builder.append(uid).append("\t").append(currentServlet.getGroupSenderNickname(
                    message.getGroupUid(), uid)); //TODO 突然发现每个群可能管理员是不同的= =
        message.response(builder.toString());
    }

    public static ShowAdmin getInstance() {
        return instance;
    }

    @Override
    public String getHelpMessage() {
        return "avalon whoisadmin|谁是管理员：显示管理员列表";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon whoisadmin|avalon 谁是管理员");
    }
}
