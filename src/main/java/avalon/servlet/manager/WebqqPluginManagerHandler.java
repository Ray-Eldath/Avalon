package avalon.servlet.manager;

import avalon.servlet.OnlineWebqqPluginPool;
import avalon.servlet.util.MojoWebqqPlugin;
import avalon.tool.pool.Constants;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static avalon.tool.pool.Constants.Address.servletScriptFile;
import static avalon.tool.pool.Constants.Basic.currentPath;
import static java.io.File.separator;

/**
 * Created by Eldath Ray on 2017/4/20 0020.
 *
 * @author Eldath Ray
 */
public class WebqqPluginManagerHandler {
    private static WebqqPluginManagerHandler instance = new WebqqPluginManagerHandler();

    public static WebqqPluginManagerHandler getInstance() {
	    if ("".equals(Constants.Basic.currentServlet.scriptFilePath()))
            return null;
        return instance;
    }

    // 保证传入的name是一定存在的Plugin的name
    JSONObject getInfo(String name) {
        return OnlineWebqqPluginPool.getInstance().getPlugin(name);
    }

    JSONObject disabled(String name) {
        String total = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(servletScriptFile), StandardCharsets.UTF_8)) {
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
            return errorHandler(e);
        }
        try (FileWriter writer = new FileWriter(new File(currentPath +
                separator + "bin" + separator + "Mojo-Webqq.pl.lock"))) {
            writer.write(total);

        } catch (IOException e) {
            return errorHandler(e);
        }
        return new JSONObject().put("message", "禁用插件" + name + "成功。请重启Mojo-Webqq以生效改动！");
    }

	JSONObject setParameter(MojoWebqqPlugin plugin, JSONObject parameters) {
		String command = "$client->load(\"" + plugin + "\"";
        String parameter = "";
        getParameterString(parameter, parameters);
        if (parameters.length() == 0)
            command += ");";
        else command += parameter + "});";
        disabled(plugin.getName());
        String content = "";
        try (BufferedReader reader = Files.newBufferedReader(
		        Paths.get(Constants.Address.servletScriptFile), StandardCharsets.UTF_8)) {
            String thisLine;
            while ((thisLine = reader.readLine()) != null) {
                if (thisLine.contains("$client = Mojo::Webqq->new();"))
                    content += "\n" + command;
                content += thisLine;
            }
        } catch (IOException e) {
            return errorHandler(e);
        }
		try (FileWriter writer = new FileWriter(Constants.Address.servletScriptFile)) {
            writer.write(command);
        } catch (IOException e) {
            errorHandler(e);
        }
        return new JSONObject().put("message", "修改插件" + plugin.getName() + "参数成功。请重启Mojo-Webqq以生效改动！");
    }

    private String getParameterString(String string, JSONObject object) {
        for (Map.Entry<String, Object> entry : object.toMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JSONObject)
                getParameterString(string, (JSONObject) value);
            string += key + "=>" + String.valueOf(value) + ",\n";
        }
        return string;
    }

    private JSONObject errorHandler(Exception e) {
        return new JSONObject().put("error", "处理请求时发生异常：" + e.toString());
    }
}
