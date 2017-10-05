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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class SQLiteDatabaseOperator implements DatabaseOperator {
	private static SQLiteDatabaseOperator instance = null;
	private static final Logger logger = LoggerFactory.getLogger(SQLiteDatabaseOperator.class);
	private Statement statement;

	public static SQLiteDatabaseOperator getInstance() {
		if (instance == null) instance = new SQLiteDatabaseOperator();
		return instance;
	}

	private SQLiteDatabaseOperator() {
		try {
			Class.forName("org.sqlite.JDBC");
			statement = DriverManager.getConnection("jdbc:sqlite:res/record.db").createStatement();
		} catch (Exception e) {
			logger.error("Fatal error while load SQLite driver: ", e);
			System.exit(-1);
		}
	}

	@Override
	public void close() {
		try {
			statement.close();
		} catch (SQLException e) {
			logger.warn("Error while saving avalon.group message to SQLite: ", e);
		}
	}

	@Override
	public boolean initDB() {
//        throw new UnsupportedOperationException("请通过复制res目录下的empty_record.db文件为record.db文件完成数据库初始化");
		String prefix = Constants.Basic.currentPath + File.separator + "res" + File.separator;
		try {
			File file = new File(prefix + "record.db");
			if (file.exists())
				if (!file.delete())
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
	public boolean addQuote(LocalDateTime time, String speaker, String content) {
		return BasicDatabaseOperator.getInstance().addQuote(statement, time, speaker, content);
	}

	@Override
	public boolean add(GroupMessage input) {
		return BasicDatabaseOperator.getInstance().add(statement, input);
	}

	@Override
	public boolean add(FriendMessage input) {
		return BasicDatabaseOperator.getInstance().add(statement, input);
	}
}
