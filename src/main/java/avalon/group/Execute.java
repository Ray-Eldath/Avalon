package avalon.group;

import avalon.util.GroupMessage;
import avalon.util.SafetyReadableWriter;
import org.python.util.PythonInterpreter;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static avalon.tool.Responder.AT;

/**
 * Created by Eldath Ray on 2017/6/11 0011.
 *
 * @author Eldath Ray
 */
public class Execute extends BaseGroupMessageResponder {
    private static final Set<String> dangerCommand = new HashSet<>();
    private static final Execute instance = new Execute();

    static {
        dangerCommand.add("exit");
        dangerCommand.add("quit");
        dangerCommand.add("shutdown");
        dangerCommand.add("reboot");
        dangerCommand.add("powershell");
        dangerCommand.add("cmd");
        dangerCommand.add("system");
        dangerCommand.add("eval");
    }

    @Override
    public void doPost(GroupMessage message) {
        // message.response("目前危险指令过滤还不完善 请勿执行危险指令，感激不尽啊= =");
        String content = message.getContent();
        String[] lines = content.replace("\r", "").split("\n");
        String[] split = lines[0].split(" ");
        if (split.length < 3) {
            message.response(AT(message) + " 指令不合法 ⊙﹏⊙!");
            return;
        }
        for (String thisDangerCommand : dangerCommand) {
            for (String thisLine : lines)
                if (thisLine.trim()
                        .toLowerCase()
                        .replaceAll("[\\pP\\p{Punct}]", "")
                        .contains(thisDangerCommand)) {
                    message.response(AT(message) + " 代码中包含危险字" + thisDangerCommand +
                            "不允许执行！若有疑问请艾特Eldath~");
                    return;
                }
        }
        String lang = split[2].toLowerCase();
        if ("python".equals(lang)) {
            PythonInterpreter interpreter = new PythonInterpreter();
            SafetyReadableWriter writer = new SafetyReadableWriter();
            SafetyReadableWriter error = new SafetyReadableWriter();
            interpreter.setOut(writer);
            interpreter.setErr(error);
            String r;
            for (int i = 1; i < lines.length; i++) {
                try {
                    interpreter.exec(lines[i]);
                } catch (Exception e) {
                    r = " Oops! 您的代码执行错误! 错误如下：\n" + e.toString();
                    message.response(AT(message) + r);
                    return;
                }
            }
            if (error.out().isEmpty()) {
                String t = writer.out().replace("\n", " ");
                r = " 提交的代码已经执行完成。执行输出：" + (t.isEmpty() ? "<无>" : t);
            } else
                r = " Oops! 您的代码执行错误! 错误如下：\n" + error.out();
            message.response(AT(message) + r);
        } else {
            message.response(AT(message) + " 指定的语言目前暂不支持~ 抱歉`(*>﹏<*)′");
        }
    }

    public static Execute getInstance() {
        return instance;
    }

    @Override
    public String getHelpMessage() {
        return "avalon execute <语言，目前仅支持Python>{换行}<程序，支持多行>：执行指定语言的程序并返回输出";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon execute \\w+");
    }
}
