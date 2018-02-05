package avalon.tool.database;

import avalon.tool.pool.Constants;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class SQLiteDatabaseOperator implements DatabaseOperator {
	private static SQLiteDatabaseOperator instance = null;
	private static final Logger logger = LoggerFactory.getLogger(SQLiteDatabaseOperator.class);
	private BasicDatabaseOperator operator;
	private Statement statement;

	public static SQLiteDatabaseOperator getInstance() {
		if (instance == null) instance = new SQLiteDatabaseOperator();
		return instance;
	}

	private SQLiteDatabaseOperator() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection("jdbc:sqlite:res/record.db");
			operator = new BasicDatabaseOperator(connection);
			statement = connection.createStatement();
		} catch (Exception e) {
			logger.error("fatal error while load SQLite driver: `" + e.getLocalizedMessage() + "`");
			Runtime.getRuntime().halt(-1);
		}
	}

	@Override
	public void close() {
		operator.close();
	}

	@Override
	public boolean initDB() {
//        throw new UnsupportedOperationException("请通过复制res目录下的empty_record.db文件为record.db文件完成数据库初始化");
		String prefix = Constants.Basic.INSTANCE.getCURRENT_PATH() + File.separator + "res" + File.separator;
		try {
			File file = new File(prefix + "record.db");
			if (file.exists() && !file.delete())
				return false;
			if (!file.createNewFile())
				return false;
			Files.copy(Paths.get(prefix + "empty_record.db"), Paths.get(prefix + "record.db"));
			return true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
}
