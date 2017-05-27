package avalon.tool.pool;

import avalon.util.AvalonPlugin;
import avalon.util.AvalonPluginInfo;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static avalon.tool.pool.ConstantPool.Address.dataPath;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Eldath Ray on 2017/5/27 0027.
 *
 * @author Eldath Ray
 */
public class AvalonPluginPool {
    private static final Path setting = Paths.get(dataPath + File.separator + "plugin" + File.separator + "plugins.json");
    private List<AvalonPluginInfo> infoList = new ArrayList<>();
    private static AvalonPluginPool instance = null;
    private static final Logger logger = getLogger(AvalonPluginPool.class);

    public static AvalonPluginPool getInstance() {
        if (instance == null)
            try {
                instance = new AvalonPluginPool();
            } catch (IOException e) {
                logger.error("error thrown while reading plugin config file: " + e.toString());
                System.exit(-1);
            }
        return instance;
    }

    private AvalonPluginPool() throws IOException {
        JSONObject main = ((JSONObject) new JSONTokener(Files.newBufferedReader(setting)).nextValue()).getJSONObject("plugins");
        Set<String> keySet = main.keySet();
        keySet.forEach(e -> {
            JSONObject o = main.getJSONObject(e);
            infoList.add(new AvalonPluginInfo(
                    e,
                    o.getString("version"),
                    o.getString("copyright"),
                    o.getString("website"),
                    o.getString("class"),
                    o.getString("file"),
                    o.getBoolean("enable")));
        });
    }

    public void load() {
        infoList.stream().filter(AvalonPluginInfo::isEnabled).forEach(e -> {
            try {
                load(e);
            } catch (Exception e1) {
                logger.warn("plugin " + e.getName() + " load failed: " + e1.toString());
            }
        });
    }

    private void load(AvalonPluginInfo info) throws
            MalformedURLException,
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException {
        AvalonPlugin plugin = (AvalonPlugin) new URLClassLoader(
                new URL[]{new URL("file:" + dataPath + File.separator +
                        "plugin" + File.separator + info.getFileName())},
                Thread.currentThread().getContextClassLoader())
                .loadClass(info.getClassString())
                .newInstance();
        plugin.main();
    }

    public List<AvalonPluginInfo> getInfoList() {
        return infoList;
    }
}
