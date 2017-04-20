package avalon.group;

import avalon.tool.pool.VariablePool;
import avalon.util.GroupMessage;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class Mo extends BaseGroupMessageResponder {
    private static Mo instance = null;

    public static Mo getInstance() {
        if (instance == null) instance = new Mo();
        return instance;
    }

    @Override
    public void doPost(GroupMessage message) {
        if (VariablePool.Mo_Reach_Max) return;
        if (VariablePool.Mo_Count >= 50) {
            message.response("哼！你们今天膜的太多啦！长者肯定会生气的！");
            VariablePool.Mo_Reach_Max = true;
            return;
        }
        String[] responseMessages = {"哈哈蛤哈",
                "你们有没有... ...就是那种... ...那种... ...诗？",
                "那首诗怎么念来着？苟利国家... ...",
                "我跟你江，你们这是要负泽任的，民白不？",
                "还是去弹夏威夷吉他吧！",
                "枸杞有养生功效，古人云：枸利果佳生食宜，气阴火服必驱之",
                "下面我们有请州长夫人演唱！"};
        message.response(responseMessages[new Random().nextInt(responseMessages.length)]);
        VariablePool.Mo_Count++;
    }

    @Override
    public String getHelpMessage() {
        return "膜*关键词：随机触发膜*语句";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("\\w?(\\+1s|-1s|膜蛤|苟|续命|州长夫人)\\w?");
    }
}
