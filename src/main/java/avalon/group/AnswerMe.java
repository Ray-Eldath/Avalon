package avalon.group;

import avalon.tool.Responder;
import avalon.util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class AnswerMe extends BaseGroupMessageResponder {
    // private static final Logger logger = LoggerFactory.getLogger(AnswerMe.class);
    private static AnswerMe instance = null;

    public static AnswerMe getInstance() {
        if (instance == null) instance = new AnswerMe();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        String sender = message.getSenderNickName();
        String content = message.getContent()
                .trim()
                .toLowerCase()
                .replaceAll("[\\pP\\p{Punct}]", "");
        String text = content;
        text = text.replaceAll(getKeyWordRegex().toString(), "");
        if ("".equals(text.replace(" ", ""))) {
            message.response("@" + sender + " 消息不能为空哦~(*∩_∩*)");
            return;
        }
        if (strIsEnglish(text)) {
            if (text.length() < 5) {
                message.response("@" + sender + " 您的消息过短~o(╯□╰)o！");
                return;
            }
        } else if (text.length() < 3) {
            message.response("@" + sender + " 您的消息过短~o(╯□╰)o！");
            return;
        }
        content = content.replaceAll(getKeyWordRegex().toString(), "");
        String responseXiaoIce = Responder.responseXiaoIce(content);
        if (responseXiaoIce == null) return;
        message.response("@" + sender + " " + responseXiaoIce);
    }

    private boolean strIsEnglish(String word) {
        for (int i = 0; i < word.length(); i++)
            if (!(word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')
                    && !(word.charAt(i) >= 'a' && word.charAt(i) <= 'z'))
                return false;
        return true;
    }

    @Override
    public String getHelpMessage() {
        return "avalon answer me / 阿瓦隆回答我 / avalon tell me / 阿瓦隆告诉我：激活智能回复功能";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon answer me |阿瓦隆回答我 |avalon tell me |阿瓦隆告诉我 ");
    }
}
