package avalon.tool;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath Ray on 2017/6/10 0010.
 *
 * @author Eldath Ray
 */
public class ServletConfigSystem implements BaseConfigSystem {
    private Map<String, Object> map = new HashMap<>();
    private static ServletConfigSystem instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ServletConfigSystem.class);

    public static ServletConfigSystem getInstance() {
        if (instance == null)
            instance = new ServletConfigSystem(Paths.get("servlet.json"));
        return instance;
    }

    private ServletConfigSystem(Path path) {
        try {
            map = ((JSONObject) new JSONTokener(Files.newBufferedReader(path)).nextValue()).toMap();
        } catch (IOException e) {
            logger.error("error while reading servletScriptFile config file: " + path.toString() + e.toString());
            System.exit(-1);
        }
    }

    @Override
    public Object get(String key) {
        return map.get(key);
    }

    @Override
    public String getString(String key) {
        Object obj = map.get(key);
        if (!(obj instanceof String))
            throw new UnsupportedOperationException("value invalid: not a String");
        return (String) obj;
    }
}
