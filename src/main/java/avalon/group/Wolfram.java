package avalon.group;

import avalon.tool.ConfigSystem;
import avalon.tool.WolframGetter;
import avalon.util.GroupMessage;
import org.eclipse.jetty.util.UrlEncoded;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static avalon.tool.Responder.AT;
import static avalon.tool.pool.ConstantPool.Basic.currentServlet;

/**
 * Created by Eldath Ray on 2017/6/11 0011.
 *
 * @author Eldath Ray
 */
public class Wolfram extends BaseGroupMessageResponder {
    private static final Logger logger = LoggerFactory.getLogger(Wolfram.class);

    private static final Wolfram instance = new Wolfram();

    @Override
    public void doPost(GroupMessage message) {
        String content = message.getContent();
        String question = content.replace("avalon tell me ", "");
        String url = "http://api.wolframalpha.com/v2/query?input=" + UrlEncoded.encodeString(question) +
                "&appid=" + ConfigSystem.getInstance().getCommandConfig("Wolfram", "App_Id");
        if (message.getContent().matches("avalon tell me [\\u4e00-\\u9fa5]")) {
            message.response(" 指令不和规范~ o(╯□╰)o");
            return;
        }
        message.response(AT(message) + " 由于消息长度过长，将会将结果私聊给您。请等待网络延迟！^_^#");
        try {
            SAXBuilder builder = new SAXBuilder();
            List<WolframGetter.WolframPod> pods = WolframGetter.get(builder.build(url).getRootElement());
            StringBuilder builder1 = new StringBuilder();
            for (WolframGetter.WolframPod thisPod : pods)
                builder1.append(thisPod.getTitle()).append("\n").append("---\n").append(thisPod.getPlaintext());
            builder1.append("\n详见：http://www.wolframalpha.com/input?i=").append(UrlEncoded.encodeString(question));
            currentServlet.responseFriend(message.getSenderUid(), builder1.toString());
        } catch (JDOMException | IOException e) {
            logger.error("exception thrown while parse XML from " + url + " " + e.toString());
        }
    }

    @Override
    public String getHelpMessage() {
        return "avalon tell me <your question>: (Only English) send your question to Wolfram Alpha and echo the return.";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon tell me \\w+");
    }

    public static Wolfram getInstance() {
        return instance;
    }
}
