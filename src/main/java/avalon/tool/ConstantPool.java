package avalon.tool;

import org.json.JSONObject;
import org.json.JSONTokener;

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
        public static final DatabaseOperator currentDatabaseOperator = SQLiteDatabaseOperator.getInstance();
    }

    public static class Address {
        public static final String webqq = addressHandle((String) ConfigSystem.getInstance()
                .getConfig("Mojo-Webqq_API_Address"));
        public static final String wechat = addressHandle((String) ConfigSystem.getInstance()
                .getConfig("Mojo-Weixin_API_Address"));

        private static String addressHandle(String address) {
            return address.endsWith("/") ? address.substring(0, address.length() - 1) : address;
        }
    }

    public static class Version {
        public static final String avalon = Basic.Version;

        private static Version instance = new Version();

        private String webqq;
        private String wechat;

        public static Version getInstance() {
            return instance;
        }

        private Version() {
            try {
                webqq = ((JSONObject) new JSONTokener(
                        new URL(Address.webqq + "/openqq/get_client_info")
                                .openStream()).nextValue()).getString("version");
                wechat = "v" + ((JSONObject) new JSONTokener(
                        new URL(wechat + "/openwx/get_client_info")
                                .openStream()).nextValue()).getString("version");
            } catch (IOException ignore) {
                webqq = "UNKNOWN";
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
        static final String Version = "0.0.1 Beta";
        public static final boolean Debug = (boolean) ConfigSystem.getInstance().getConfig("Debug");
        public static final long startTime = System.currentTimeMillis();
        public static final int pid = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
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
