package avalon.tool.system;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static avalon.tool.pool.ConstantPool.Address.dataPath;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class RunningDataSystem {
    private static final Path file = Paths.get(dataPath + File.separator + "data.properties");
    private static Properties properties = new Properties();

	private static RunningDataSystem instance = null;

	public static RunningDataSystem getInstance() {
		if (instance == null) instance = new RunningDataSystem();
		return instance;
    }

	private RunningDataSystem() {
		try {
            if (Files.notExists(file)) {
                properties.setProperty("friendId", "0");
                properties.setProperty("groupId", "0");
                properties.setProperty("group_message_recorded_count", "0");
                properties.setProperty("friend_message_recorded_count", "0");
                Files.createFile(file);
                save();
            }
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
