package extend.scheduler;

import jdk.nashorn.api.scripting.URLReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath on 2017/1/30 0030.
 *
 * @author Eldath
 */
public class ShowMsg {
    private static final Logger logger = LoggerFactory.getLogger(ShowMsg.class);
    private static final String urlString =
            "http://git.oschina.net/ProgramLeague/Image/raw/master/Avalon/ShowMsg.properties";
    private static final LocalDateTime now = LocalDateTime.now();

    public ShowMsg() {
        // Read part
        String thisLine;
        String[] split;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.clear();
        try (BufferedReader br = new BufferedReader(new URLReader(new URL(urlString), StandardCharsets.UTF_8))) {
            while ((thisLine = br.readLine()) != null) {
                if (!thisLine.contains("=") || thisLine.charAt(0) == '#') continue;
                split = thisLine.split("=");
                hashMap.put(split[0], split[1]);
            }
        } catch (IOException e) {
            logger.info("IOException while ShowMsg: ", e);
        }
        for (Map.Entry stringEntry : hashMap.entrySet()) {
            LocalDateTime key = LocalDateTime.parse(now.getYear() + "-" + stringEntry.getKey());
            String value = ((String) stringEntry.getValue()).replace("\\n", "\n");
            String nowTime = now.getYear() + "年" +
                    now.getMonthValue() + "月" +
                    now.getDayOfMonth() + "日 " +
                    now.getHour() + "时" +
                    now.getMinute() + "分" +
                    now.getSecond() + "秒";
            Scheduler.scheduleTask(key, new ShowMsgTask("现在是" + nowTime + "\n" + value));
        }
    }
}
