package tool;

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
}
