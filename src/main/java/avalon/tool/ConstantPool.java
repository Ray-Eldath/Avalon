package avalon.tool;

import avalon.data.ConfigSystem;

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
        public static final String APIServer = (String) ConfigSystem.getInstance()
                .getConfig("Mojo-Webqq_API_Address");
        public static final String weChatAPIServer = (String) ConfigSystem.getInstance()
                .getConfig("Mojo-Weixin_API_Address");
    }

    public static class Basic {
        public static final String Version = "0.0.1 Beta";
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
