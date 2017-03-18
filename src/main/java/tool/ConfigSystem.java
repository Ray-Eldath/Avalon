package tool;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Eldath Ray on 2017/3/17.
 *
 * @author Eldath Ray
 * @since v0.0.1 Beta
 */
public class ConfigSystem {
    private static ConfigSystem instance = null;
    private static Map<String, Object> allConfigs = new HashMap<>();
    private static JSONObject pluginConfig = new JSONObject();
    private static JSONObject allCommandAllow = new JSONObject();
    private static final Logger logger = LoggerFactory.getLogger(ConfigSystem.class);

    public static ConfigSystem getInstance() {
        if (instance == null) instance = new ConfigSystem();
        return instance;
    }

//    static {
//        allConfigs.put("", "");
//    }

    private ConfigSystem() {
        try {
            JSONObject object = (JSONObject) new JSONTokener(new FileReader(new File("config.json")))
                    .nextValue();
            allConfigs = new HashMap<>(object.toMap());
            allCommandAllow = (JSONObject) object.get("plugin_allowed_account");
            pluginConfig = (JSONObject) object.get("plugin_config");
        } catch (FileNotFoundException e) {
            logger.error("Exception thrown while init ConfigSystem: ", e);
        }
    }

    public Object getConfig(String key) {
        return allConfigs.get(key);
    }

    public long[] getCommandAllowArray(String commandName) {
        JSONArray convert = (JSONArray) allCommandAllow.get(commandName);
        long[] result = new long[convert.length()];
        for (int i = 0; i < result.length; i++) {
            Object tmp = convert.get(i);
            result[i] = ((Integer) tmp).longValue();
        }
        return result;
    }

    public Object getCommandConfig(String commandName, String key) {
        return pluginConfig.getJSONObject(commandName).get(key);
    }
}
