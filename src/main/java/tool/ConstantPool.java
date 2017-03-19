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
}
