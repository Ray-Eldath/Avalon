package avalon.tool.database;

import avalon.tool.system.DatabaseConfigSystem;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class MySQLDatabaseOperator implements DatabaseOperator {
    private static MySQLDatabaseOperator instance = new MySQLDatabaseOperator();
    private static Statement statement;

    public static MySQLDatabaseOperator getInstance() {
        return instance;
    }

    private MySQLDatabaseOperator() {
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            String originHost = DatabaseConfigSystem.getInstance().getString("Host");
            String url = (originHost.endsWith("/") ? originHost.substring(0, originHost.length() - 1) :
                    originHost + "/") + DatabaseConfigSystem.getInstance().getString("Database") + "?serverTimezone=UTC";
            String username = DatabaseConfigSystem.getInstance().getString("Username");
            String password = DatabaseConfigSystem.getInstance().getString("Password");
            Connection conn = DriverManager.getConnection(url, username, password);
            statement = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean initDB() {
        throw new UnsupportedOperationException("未知原因导致一直报SQL格式不对，但我使用mysql命令行工具却未发现任何问题。暂无法解决。");
//        try {
//            List<String> lines = Files.readAllLines(Paths.get(ConstantPool.Basic.currentPath + File.separator + "res" +
//                    File.separator + "record_mysql.sql"), StandardCharsets.UTF_8);
//            StringBuilder builder = new StringBuilder();
//            for (String thisLine : lines) {
//                if (thisLine.endsWith(";")) {
//                    builder.append(thisLine.replaceAll("\\s+", " "));
//                    System.out.println(builder.toString());
//                    statement.execute(builder.toString());
//                    builder = new StringBuilder();
//                } else
//                    builder.append(thisLine.replaceAll("\\s+", " "));
//            }
//            statement.executeBatch();
//            statement.clearBatch();
//            return true;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public boolean add(GroupMessage input) {
        return BasicDatabaseOperator.getInstance().add(statement, input);
    }

    @Override
    public boolean add(FriendMessage input) {
        return BasicDatabaseOperator.getInstance().add(statement, input);
    }

    @Override
    public void close() {
    }
}
