package avalon.tool.pool;

import avalon.util.Plugin;
import avalon.util.PluginParameter;
import avalon.util.PluginParameterType;
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
    private List<Plugin> plugins = new ArrayList<>();
    private HashMap<String, JSONObject> pluginsMap = new HashMap<>();

    private static OnlineWebqqPluginPool ourInstance = new OnlineWebqqPluginPool();

    public static OnlineWebqqPluginPool getInstance() {
        return ourInstance;
    }

    private OnlineWebqqPluginPool() {
        try {
            JSONArray array = ((JSONObject) new JSONTokener(new URL("https://raw.githubusercontent.com/" +
                    "Ray-Eldath/Avalon-Plugin-Info-Getter/master/plugins.json")
                    .openStream()).nextValue()).getJSONArray("plugins");
            int arrayLength = array.length();
            for (int i = 0; i < arrayLength; i++) {
                JSONObject thisPlugin = array.getJSONObject(i);
                int id = thisPlugin.getInt("id");
                String name = thisPlugin.getString("name");
                String description = thisPlugin.getString("description");
                String developer = thisPlugin.getString("developer");
                pluginsMap.put(name, thisPlugin);
                Plugin plugin = new Plugin(id, name, description, developer);
                plugin.addParameter(getParameters(thisPlugin.getJSONObject("parameters").toMap(), new ArrayList<>()));
                plugins.add(plugin);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<PluginParameter> getParameters(Map<String, Object> parameters, List<PluginParameter> list) {
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
                PluginParameterType type = PluginParameterType.from(typeString);
                PluginParameter parameter;
                if (PluginParameterType.isParameters(type))
                    parameter = new PluginParameter(key, type, PluginParameterType.from(typeString.split(":")[1]));
                else
                    parameter = new PluginParameter(key, type);
                list.add(parameter);
            }
        }
        return list;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public JSONObject getPlugin(String name) {
        return pluginsMap.get(name);
    }

    public boolean isPluginExist(String name) {
        return pluginsMap.containsKey(name);
    }
}
