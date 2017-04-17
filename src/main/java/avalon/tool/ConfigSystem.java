package avalon.tool;

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

/**
 * Created by Eldath Ray on 2017/3/17.
 *
 * @author Eldath Ray
 * @since v0.0.1 Beta
 */
public class ConfigSystem {
    private static ConfigSystem instance = null;
    private static Map<String, Object> allConfigs = new HashMap<>();
    private static Map<String, Object> pluginConfigs = new HashMap<>();
    private static JSONObject allCommandAllow = new JSONObject();
    private static final Logger logger = LoggerFactory.getLogger(ConfigSystem.class);

    public static ConfigSystem getInstance() {
        if (instance == null) instance = new ConfigSystem();
        return instance;
    }

    private ConfigSystem() {
        try {
            JSONObject object = (JSONObject) new JSONTokener(new FileReader(new File("config.json")))
                    .nextValue();
            allConfigs = jsonObjectToMap(object);
            allCommandAllow = (JSONObject) object.get("plugin_allowed_account");
            pluginConfigs = jsonObjectToMap((JSONObject) object.get("plugin_config"));
        } catch (FileNotFoundException e) {
            logger.error("Exception thrown while init ConfigSystem: ", e);
        }
    }

    private Map<String, Object> jsonObjectToMap(JSONObject object) {
        Map<String, Object> result = new HashMap<>();
        JSONArray names = object.names();
        for (int i = 0; i < object.length(); i++) {
            String key = names.get(i).toString();
            Object thisObject = object.get(key);
            result.put(key, thisObject);
        }
        return result;
    }

    public Object getConfig(String key) {
        return allConfigs.get(key);
    }

    public Object[] getConfigArray(String key) {
        JSONArray array = (JSONArray) allConfigs.get(key);
        Object[] result = new Object[array.length()];
        Object thisObject;
        for (int i = 0; i < array.length(); i++) {
            thisObject = array.get(i);
            System.out.println(thisObject.getClass().getName());
            result[i] = thisObject;
        }
        return result;
    }

    public long[] getCommandAllowArray(String commandName) {
        JSONArray convert = allCommandAllow.getJSONArray(commandName);
        long[] result = new long[convert.length()];
        for (int i = 0; i < convert.length(); i++)
            result[i] = convert.getLong(i);
        return result;
    }

    public Object getCommandConfig(String commandName, String key) {
        return ((JSONObject) pluginConfigs.get(commandName)).get(key);
    }

    public Object[] getCommandConfigArray(String commandName, String key) {
        JSONArray convert = ((JSONObject) pluginConfigs.get(commandName)).getJSONArray(key);
        Object[] result = new Object[convert.length()];
        for (int i = 0; i < convert.length(); i++)
            result[i] = convert.get(i);
        return result;
    }
}
