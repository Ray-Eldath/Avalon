package avalon.manager;

import avalon.tool.pool.WebqqPluginPool;
import avalon.util.Plugin;
import avalon.util.PluginParameter;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static avalon.tool.pool.ConstantPool.Address.perlFileOfWebqq;
import static avalon.tool.pool.ConstantPool.Basic.currentPath;
import static java.io.File.separator;

/**
 * Created by Eldath Ray on 2017/4/20 0020.
 *
 * @author Eldath Ray
 */
public class WebqqPluginManagerHandler {
    private static WebqqPluginManagerHandler instance = new WebqqPluginManagerHandler();

    public static WebqqPluginManagerHandler getInstance() {
        return instance;
    }

    // 保证传入的name是一定存在的Plugin的name
    JSONObject getInfo(String name) {
        return WebqqPluginPool.getInstance().getPlugin(name);
    }

    JSONObject disabled(String name) {
        String total = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(perlFileOfWebqq))) {
            String thisLine;
            boolean get = false;
            while ((thisLine = reader.readLine()) != null) {
                thisLine = thisLine.replaceAll("\\s", "");
                if (thisLine.startsWith("#"))
                    continue;
                if (thisLine.contains("$client->load(\"" + name + "\""))
                    get = true;
                if (get) {
                    if (thisLine.contains("});"))
                        get = false;
                } else total += thisLine + "\n";
            }
        } catch (IOException e) {
            return new JSONObject().put("error", "处理请求时发生异常：" + e.toString());
        }
        try (FileWriter writer = new FileWriter(new File(currentPath +
                separator + "bin" + separator + "Mojo-Webqq.pl.lock"))) {
            writer.write(total);

        } catch (IOException e) {
            return new JSONObject().put("error", "处理请求时发生异常：" + e.toString());
        }
        return new JSONObject().put("message", "禁用插件" + name + "成功。请重启Mojo-Webqq以生效改动！");
    }

    JSONObject setParameter(Plugin plugin, Map<PluginParameter, Object> parameters) {
        String command = "$client->load(\"" + plugin + "\"";
        if (parameters.isEmpty())
            command += ");";
        else {
            String parameterString;
            for ()
        }
    }
}
