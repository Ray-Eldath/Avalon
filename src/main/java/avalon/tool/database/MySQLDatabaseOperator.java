package avalon.tool.database;

import avalon.tool.system.Configs;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class MySQLDatabaseOperator implements DatabaseOperator {
	private static Statement statement;
	private static BasicDatabaseOperator operator;

	private static MySQLDatabaseOperator instance = new MySQLDatabaseOperator();

	public static MySQLDatabaseOperator getInstance() {
		return instance;
	}

	private MySQLDatabaseOperator() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			JSONObject setting = Configs.INSTANCE.getJSONObject("database");
			String originHost = setting.getString("host");
			String url = (originHost.endsWith("/") ? originHost.substring(0, originHost.length() - 1) :
					originHost + "/") + setting.getString("database") + "?serverTimezone=UTC";
			String username = setting.getString("username");
			String password = setting.getString("password");

			Connection conn = DriverManager.getConnection(url, username, password);
			statement = conn.createStatement();
			operator = new BasicDatabaseOperator(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean initDB() {
		throw new UnsupportedOperationException("未知原因导致一直报SQL格式不对，但我使用mysql命令行工具却未发现任何问题。暂无法解决。");
//        try {
//            List<String> lines = Files.readAllLines(Paths.get(ConstantPool.Basic.CURRENT_PATH + File.separator + "res" +
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
	public boolean exist(Table table, String condition) {
		return operator.exist(statement, table, condition);
	}

	@Override
	public boolean addQuote(int hashCode, String speaker, String content) {
		return operator.addQuote(hashCode, speaker, content);
	}

	@Override
	public boolean add(GroupMessage input) {
		return operator.add(input);
	}

	@Override
	public boolean add(FriendMessage input) {
		return operator.add(input);
	}

	@Override
	public void close() {
	}
}
