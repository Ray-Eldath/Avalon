package tool;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Eldath Ray on 2017/3/17.
 *
 * @author Eldath Ray
 * @since v0.0.1 Beta
 */
public class ConfigSystem {
    private static HashMap<String, Object> allConfigs = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ConfigSystem.class);

//    static {
//        allConfigs.put("", "");
//    }

    public ConfigSystem() {
        try {
            JSONObject object = (JSONObject) new JSONTokener(new FileReader(new File("config.json"))).nextValue();
            allConfigs = new HashMap<>(object.toMap());
        } catch (FileNotFoundException e) {
            logger.error("Exception thrown while init ConfigSystem: ", e);
        }
    }

    // Lambda, lambda, GO!
    public Optional<Object> get(String key) {
        return Optional.ofNullable(allConfigs.get(key));
    }
}
