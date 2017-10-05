package avalon.tool.system;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath Ray on 2017/3/17.
 *
 * @author Eldath Ray
 * @since v0.0.1 Beta
 */
public class Config implements BaseConfigSystem {
	private static Config instance = null;
	private static JSONObject root;
	private static Map<String, Object> allConfigs = new HashMap<>();
	private static Map<String, Object> pluginConfigs = new HashMap<>();
	private static final Logger logger = LoggerFactory.getLogger(Config.class);

	public static Config instance() {
		if (instance == null) instance = new Config();
		return instance;
	}

	private Config() {
		try {
			String path = new File("").getCanonicalPath(); // 这里不用常量池是因为初始化的问题。反正别改。
			root = (JSONObject) new JSONTokener(new FileReader(
					new File(path + File.separator + "config.json"))).nextValue();
			allConfigs = jsonObjectToMap(root);
			pluginConfigs = jsonObjectToMap((JSONObject) root.get("plugin_config"));
		} catch (IOException e) {
			logger.error("Exception thrown while init Config: ", e);
		}
	}

	private Map<String, Object> jsonObjectToMap(JSONObject object) {
		Map<String, Object> result = new HashMap<>();
		JSONArray names = object.names();
		for (int i = 0; i < object.length(); i++) {
			String key = names.get(i).toString();
			if (key.contains("comment"))
				continue;
			Object thisObject = object.get(key);
			result.put(key, thisObject);
		}
		return result;
	}

	@Override
	public Object get(String key) {
		return allConfigs.get(key);
	}

	@Override
	public String getString(String key) {
		Object obj = allConfigs.get(key);
		if (!(obj instanceof String))
			throw new UnsupportedOperationException("value invalid: not a String");
		return (String) obj;
	}

	public JSONObject getJSONObject(String key) {
		return root.getJSONObject(key);
	}

	public Object[] getConfigArray(String key) {
		JSONArray array = (JSONArray) allConfigs.get(key);
		Object[] result = new Object[array.length()];
		Object thisObject;
		for (int i = 0; i < array.length(); i++) {
			thisObject = array.get(i);
			result[i] = thisObject;
		}
		return result;
	}

	public Object getCommandConfig(String commandName, String key) {
		JSONObject object = (JSONObject) pluginConfigs.get(commandName);
		return object.has(key) ? object.get(key) : null;
	}

	public boolean isCommandEnable(String name) {
		return (boolean) getCommandConfig(name, "enable");
	}

	public Object[] getCommandConfigArray(String commandName, String key) {
		JSONArray convert = ((JSONObject) pluginConfigs.get(commandName)).getJSONArray(key);
		Object[] result = new Object[convert.length()];
		for (int i = 0; i < convert.length(); i++)
			result[i] = convert.get(i);
		return result;
	}
}
