package avalon.group;

import avalon.tool.ConstantPool;
import avalon.util.GroupMessage;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Version extends BaseGroupMessageResponder {
    private static Version instance = null;

    public static Version getInstance() {
        if (instance == null) instance = new Version();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        try {
            String webqqVersion = "v" + ((JSONObject) new JSONTokener(
                    new URL(ConstantPool.Address.APIServer + "/openqq/get_client_info")
                            .openStream()).nextValue()).getString("version");
            String weixinVersion;
            try {
                weixinVersion = "v" + ((JSONObject) new JSONTokener(
                        new URL(ConstantPool.Address.weChatAPIServer + "/openwx/get_client_info")
                                .openStream()).nextValue()).getString("version");
            } catch (IOException e) {
                weixinVersion = "UNKNOWN";
            }
            String messageToSaid = "Hi, I'm Avalon.\n" +
                    "我是阿瓦隆，QQ群机器人。\n" +
                    "我的名字和头像均取自《Implosion》，我的父亲是Mojo-Webqq。\n" +
                    "我由Eldath Ray进行二次开发。\n" +
                    "我在GitHub上开源，欢迎访问我的仓库：https://github.com/ProgramLeague/Avalon\n" +
                    "Mojo-Webqq Version: " + webqqVersion + "\tMojo-Weixin Version: " + weixinVersion +
                    "\tAvalon Version: v" + ConstantPool.Basic.Version;
            message.response(messageToSaid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelpMessage() {
        return "avalon version / avalon 版本：显示版本信息";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon version|avalon 版本");
    }
}
