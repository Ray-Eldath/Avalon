package avalon.tool.database;

import avalon.tool.pool.Constants;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @deprecated v1.2.3起停止对SQLite的支持，而使用H2替代之。
 * 全功能测试通过。2018.2.8
 */
public class SQLiteDatabaseOperator implements DatabaseOperator {
	private static SQLiteDatabaseOperator instance = null;
	private static final Logger logger = LoggerFactory.getLogger(SQLiteDatabaseOperator.class);
	private BasicDatabaseOperator operator;
	private Statement statement;

	private String fileS = Constants.Basic.INSTANCE.getCURRENT_PATH() + "/res/record.sqlite.db";

	@NotNull
	public static SQLiteDatabaseOperator getInstance() {
		if (instance == null) instance = new SQLiteDatabaseOperator();
		return instance;
	}

	private SQLiteDatabaseOperator() {
		try {
			if (!dbFileExist()) {
				logger.info("SQLite database not exist. Try to initialize database...");
				if (initDB())
					logger.info("Successfully initialize database.");
				else
					logger.error("Failed to initialize database. Please try to initialize yourself.");
			}
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection("jdbc:sqlite:res/record.sqlite.db");
			operator = new BasicDatabaseOperator(connection);
			statement = connection.createStatement();
		} catch (Exception e) {
			logger.error("fatal error while load SQLite database driver: `" + e.getLocalizedMessage() + "`");
			Runtime.getRuntime().halt(-1);
		}
	}

	@Override
	public void close() {
		operator.close();
	}

	@NotNull
	@Override
	public Statement statement() {
		return statement;
	}

	@Override
	public boolean initDB() {
//        throw new UnsupportedOperationException("请通过复制res目录下的empty_record.db文件为record.db文件完成数据库初始化");
		String emptyFileS = Constants.Basic.INSTANCE.getCURRENT_PATH() + "/res/EMPTY_record.sqlite.db";
		try {
			if (dbFileExist())
				return true;
			Files.copy(Paths.get(emptyFileS), Paths.get(fileS));
			return true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean dbFileExist() {
		return Files.exists(Paths.get(fileS));
	}

	@Override
	public boolean exist(@NotNull Table table, @NotNull String condition) {
		return operator.exist(statement, table, condition);
	}

	@Override
	public int count(@NotNull Table table) {
		return operator.count(statement, table);
	}

	@Override
	public boolean addQuote(int hashCode, @NotNull String speaker, @NotNull String content) {
		return operator.addQuote(hashCode, speaker, content);
	}

	@Override
	public boolean add(@NotNull GroupMessage input) {
		return operator.add(input);
	}

	@Override
	public boolean add(@NotNull FriendMessage input) {
		return operator.add(input);
	}
}
