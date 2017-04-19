package avalon.tool;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class GenerateDataFile {
    private static Properties properties = new Properties();

    static {
        properties.put("group_message_recorded_count", String.valueOf(0));
        properties.put("friend_message_recorded_count", String.valueOf(0));
        properties.put("groupId", String.valueOf(0));
        properties.put("friendId", String.valueOf(0));
    }

    public static void main(String[] args) throws Exception {
        new GenerateDataFile();
        Path file = Paths.get("data.properties");
        properties.store(Files.newBufferedWriter(file, StandardCharsets.UTF_8),
                "!!DO NOT DELETE THIS FILE!!This file is generated for Avalon System. " +
                        "It storage some data that need persisted.");
    }
}
