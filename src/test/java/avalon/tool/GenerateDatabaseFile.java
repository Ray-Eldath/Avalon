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
public class GenerateDatabaseFile {
    private static Properties properties = new Properties();
    private static final String datasource = "mysql";

    static {
        if ("mysql".equals(datasource)) {
            System.out.println("Datasource: MySQL");
            properties.put("DataSource", "mysql");
            properties.put("Host", "jdbc:mysql://localhost:3306");
            properties.put("Database", "avalon");
            properties.put("Username", "avalon");
            properties.put("Password", "123");
        } else if ("sqlite".equals(datasource)) {
            System.out.println("Datasource: Sqlite");
            properties.put("DataSource", "sqlite");
        }
    }

    public static void main(String[] args) throws Exception {
        new GenerateDatabaseFile();
        Path file = Paths.get("database.properties");
        properties.store(Files.newBufferedWriter(file, StandardCharsets.UTF_8),
                "!!DO NOT DELETE THIS FILE!!This file is generated for Avalon System. " +
                        "It storage config about database.");
    }
}
