package scheduler;

import jdk.nashorn.api.scripting.URLReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath on 2017/1/30 0030.
 *
 * @author Eldath
 */
public class ShowMsg extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ShowMsg.class);
    private HashMap<String, String> hashMap = new HashMap<>();
    private static final String urlString = "";
    private static final String nowYear = String.valueOf(LocalDate.now().getYear());

    public ShowMsg() {
        // Read part
        String thisLine;
        String[] split;
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
            String key = nowYear + "-" + stringEntry.getKey();
            String value = ((String) stringEntry.getValue()).replace("\\n", "\n");
            Response.responseGroup("617118724",
                    "现在是" + LocalDateTime.parse(key).toString().replace("T", " ") + "\n" + value);
        }
    }

    @Override
    public void run() {

    }
}
