package avalon.tool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static avalon.tool.pool.ConstantPool.Basic.currentPath;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class RunningData {
    private static final Path file = Paths.get(currentPath + File.separator + "data.properties");
    private static Properties properties = new Properties();

    private static RunningData ourInstance = new RunningData();

    public static RunningData getInstance() {
        return ourInstance;
    }

    private RunningData() {
        try {
            properties.load(Files.newBufferedReader(file, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(String key, String value) {
        properties.put(key, value);
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public void save() {
        try {
            properties.store(Files.newBufferedWriter(file, StandardCharsets.UTF_8),
                    "!!DO NOT DELETE THIS FILE!!This file is generated for Avalon System. " +
                            "It storage some data that need persisted.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
