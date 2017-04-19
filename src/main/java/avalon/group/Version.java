package avalon.group;

import avalon.tool.ConstantPool;
import avalon.util.GroupMessage;

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
        String messageToSaid = "Hi, I'm Avalon.\n" +
                "我是阿瓦隆，QQ群机器人。\n" +
                "我的名字和头像均取自《Implosion》，我的父亲是Mojo-Webqq。\n" +
                "我由Ray Eldath进行二次开发。\n" +
                "我在GitHub上开源，欢迎访问我的仓库：https://github.com/Ray-Eldath/Avalon\n" +
                "Mojo-Webqq Version: v" + ConstantPool.Version.getInstance().webqq() + "\tMojo-Weixin Version: v" +
                ConstantPool.Version.getInstance().wechat() + "\tAvalon Version: v" + ConstantPool.Version.avalon;
        message.response(messageToSaid);
    }

    @Override
    public String getHelpMessage() {
        return "avalon version|版本：显示版本信息";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon version|avalon 版本");
    }
}
