package avalon.servlet;

import avalon.servlet.util.MojoWebqqPlugin;
import avalon.servlet.util.MojoWebqqPluginParameter;
import avalon.servlet.util.MojoWebqqPluginParameterType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Eldath Ray on 2017/4/20 0020.
 *
 * @author Eldath Ray
 */
public class OnlineWebqqPluginPool {
	private List<MojoWebqqPlugin> plugins = new ArrayList<>();
	private HashMap<String, JSONObject> pluginsMap = new HashMap<>();

    private static OnlineWebqqPluginPool ourInstance = new OnlineWebqqPluginPool();

    public static OnlineWebqqPluginPool getInstance() {
        return ourInstance;
    }

    private OnlineWebqqPluginPool() {
        try {
            JSONArray array = ((JSONObject) new JSONTokener(new URL("https://raw.githubusercontent.com/" +
		            "Ray-Eldath/Avalon-MojoWebqqPlugin-Info-Getter/master/plugins.json")
		            .openStream()).nextValue()).getJSONArray("plugins");
            int arrayLength = array.length();
            for (int i = 0; i < arrayLength; i++) {
                JSONObject thisPlugin = array.getJSONObject(i);
                int id = thisPlugin.getInt("id");
                String name = thisPlugin.getString("name");
                String description = thisPlugin.getString("description");
                String developer = thisPlugin.getString("developer");
                pluginsMap.put(name, thisPlugin);
	            MojoWebqqPlugin plugin = new MojoWebqqPlugin(id, name, description, developer);
	            plugin.addParameter(getParameters(thisPlugin.getJSONObject("parameters").toMap(), new ArrayList<>()));
                plugins.add(plugin);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	private static List<MojoWebqqPluginParameter> getParameters(Map<String, Object> parameters, List<MojoWebqqPluginParameter> list) {
		if (parameters.size() == 0)
            return Collections.emptyList();
        for (Map.Entry<String, Object> thisEntry : parameters.entrySet()) {
            String key = thisEntry.getKey();
            Object value = thisEntry.getValue();
            if (value instanceof HashMap)
                //noinspection unchecked
                getParameters((HashMap<String, Object>) value, list);
            else {
                String typeString = (String) value;
	            MojoWebqqPluginParameterType type = MojoWebqqPluginParameterType.from(typeString);
	            MojoWebqqPluginParameter parameter;
	            if (MojoWebqqPluginParameterType.isParameters(type))
		            parameter = new MojoWebqqPluginParameter(key, type, MojoWebqqPluginParameterType.from(typeString.split(":")[1]));
	            else
		            parameter = new MojoWebqqPluginParameter(key, type);
	            list.add(parameter);
            }
        }
        return list;
    }

	public List<MojoWebqqPlugin> getPlugins() {
		return plugins;
    }

    public JSONObject getPlugin(String name) {
        return pluginsMap.get(name);
    }

    public boolean isPluginExist(String name) {
        return pluginsMap.containsKey(name);
    }
}
