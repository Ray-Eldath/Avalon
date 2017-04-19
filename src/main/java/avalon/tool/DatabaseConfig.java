package avalon.tool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class DatabaseConfig {
    private static DatabaseConfig instance = new DatabaseConfig();
    private static Properties properties;

    public static DatabaseConfig getInstance() {
        return instance;
    }

    private DatabaseConfig() {
        new ConstantPool.Basic();
        properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(Paths.get(ConstantPool.Basic.currentPath +
                    File.separator + "database.properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
