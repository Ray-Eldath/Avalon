package tool;

import command.APIManager;
import main.MainServlet;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath Ray on 2017/3/17.
 *
 * @author Eldath Ray
 * @since v0.0.1 Beta
 */
public class GenerateConfigFile {
    private static HashMap<String, Object> allConfigs = new HashMap<>();

    private static HashMap<String,Object> pluginAllowPeoples=new HashMap<>();

    static {
        allConfigs.put("follow_group", MainServlet.followGroup);
        pluginAllowPeoples.put("APIManager", APIManager.allowPeople);
        allConfigs.put("plugin_allow_peoples",pluginAllowPeoples);
    }

    public static void main(String[] args) {
        JSONObject object = new JSONObject();
        for (Object m : allConfigs.entrySet()) {
            Map.Entry mapEntry = (Map.Entry) m;
            String key = (String) mapEntry.getKey();
            Object value = mapEntry.getValue();
            object.append(key, value);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("config.json"), StandardCharsets.UTF_8)) {
            object.write(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
