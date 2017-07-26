package avalon.extend;

import avalon.model.ShowMsgTask;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static avalon.extend.Scheduler.scheduleTask;

/**
 * Created by Eldath on 2017/1/30 0030.
 *
 * @author Eldath
 */
public class ShowMsg {
    private static final Logger logger = LoggerFactory.getLogger(ShowMsg.class);
    private static final LocalDateTime now = LocalDateTime.now();

    public ShowMsg() {
        try (FileReader reader = new FileReader("ShowMsg.json")) {
            JSONObject object = (JSONObject) new JSONTokener(reader).nextValue();
            JSONArray names = object.names();
            for (Object name : names) {
                String thisName = (String) name;
                JSONArray thisArray = object.getJSONArray(thisName);
                StringBuilder builder = new StringBuilder("历史上的今天：");
                for (Object o : thisArray)
                    builder.append("\n").append((String) o);
                scheduleTask(LocalDate.parse(now.getYear() + "-" + thisName), new ShowMsgTask(builder.toString()));
            }
        } catch (IOException e) {
            logger.warn("error while read ShowMsg.json: " + e.toString());
        }
    }
}
