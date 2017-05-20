package avalon.tool.pool;

import avalon.tool.ConfigSystem;
import avalon.tool.DatabaseConfig;
import avalon.tool.database.DatabaseOperator;
import avalon.tool.database.MySQLDatabaseOperator;
import avalon.tool.database.SQLiteDatabaseOperator;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;

/**
 * Created by Eldath on 2017/2/3 0003.
 *
 * @author Eldath
 */
public class ConstantPool {
    private ConstantPool() {
    }

    public static class Database {
        private static final String Datasource = DatabaseConfig.getInstance().get("DataSource");
        public static final DatabaseOperator currentDatabaseOperator = "mysql".equals(Datasource) ?
                MySQLDatabaseOperator.getInstance() : SQLiteDatabaseOperator.getInstance();
    }

    public static class Address {
        public static final String webqq = addressHandle((String) ConfigSystem.getInstance()
                .getConfig("Mojo-Webqq_API_Address"));
        public static final String wechat = addressHandle((String) ConfigSystem.getInstance()
                .getConfig("Mojo-Weixin_API_Address"));
        public static final String perlFileOfWebqq = Basic.currentPath +
                File.separator + "bin" + File.separator + "Mojo-Webqq.pl";
        public static final String perlFileOfWechat = Basic.currentPath +
                File.separator + "bin" + File.separator + "Mojo-Weixin.pl";

        private static String addressHandle(String address) {
            return address.endsWith("/") ? address.substring(0, address.length() - 1) : address;
        }
    }

    public static class Version {
        public static final String avalon = Basic.Version;

        private static Version instance = null;

        private String webqq;
        private String wechat;

        public static Version getInstance() {
            if (instance == null) instance = new Version();
            return instance;
        }

        private Version() {
            try {
                webqq = ((JSONObject) new JSONTokener(
                        new URL(Address.webqq + "/openqq/get_client_info")
                                .openStream()).nextValue()).getString("version");
            } catch (IOException ignore) {
                webqq = "UNKNOWN";
            }
            try {
                wechat = ((JSONObject) new JSONTokener(
                        new URL(Address.wechat + "/openwx/get_client_info")
                                .openStream()).nextValue()).getString("version");
            } catch (IOException ignore) {
                wechat = "UNKNOWN";
            }
        }

        public String webqq() {
            return webqq;
        }

        public String wechat() {
            return wechat;
        }
    }

    public static class Basic {
        static final String Version = "0.0.1b";
        public static final boolean Debug = (boolean) ConfigSystem.getInstance().getConfig("Debug");
        public static final long startTime = System.currentTimeMillis();
        public static final int pid = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        public static final String currentPath;

        static {
            String path1;
            try {
                path1 = new File("").getCanonicalPath();
            } catch (IOException e) {
                System.out.println("Fatal error: " + e.toString());
                path1 = "";
                System.exit(-1);
            }
            currentPath = path1;
        }
    }

    public static class Setting {
        public static final boolean Block_Words_Punishment_Mode_Enabled =
                (boolean) ConfigSystem.getInstance().getConfig("Block_Words_Punishment_Mode_Enabled");
        public static final int Block_Words_Punish_Frequency =
                (int) ConfigSystem.getInstance().getConfig("Block_Words_Punish_Frequency");
    }

    public static class GameMode {
        public static final boolean IsEnabled = (boolean) ConfigSystem.getInstance().getConfig("Game_Mode_Enabled");
    }
}
