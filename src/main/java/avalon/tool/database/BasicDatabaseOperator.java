package avalon.tool.database;

import avalon.tool.system.RunningData;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class BasicDatabaseOperator implements Closeable {
	private static final Logger logger = LoggerFactory.getLogger(BasicDatabaseOperator.class);
	private static int groupId = RunningData.getInstance().getInt("group_id");
	private static int friendId = RunningData.getInstance().getInt("friend_id");

	private static final BasicDatabaseOperator instance = new BasicDatabaseOperator();

	public static BasicDatabaseOperator getInstance() {
		return instance;
	}

	private BasicDatabaseOperator() {
	}

	public boolean add(Statement statement, GroupMessage message) {
		try {
			String value =
					String.format("('%s', %d, '%s', %d, '%s', '%s')",
							message.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " "),
							message.getSenderUid(),
							message.getSenderNickName(),
							message.getGroupUid(),
							message.getGroupName(),
							UrlEncoded.encodeString(message.getContent()));
			statement.executeUpdate(
					"INSERT INTO group_ (time, senderUid, senderNickname, groupUid, groupName, content) VALUES " + value);
		} catch (SQLException e) {
			logger.warn("error while saving group message to database: ", e);
			return false;
		}
		return true;
	}

	public boolean add(Statement statement, FriendMessage message) {
		try {
			String value =
					String.format("('%s', %d, '%s', '%s')",
							message.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " "),
							message.getSenderUid(), message.getSenderNickName(),
							UrlEncoded.encodeString(message.getContent()));
			statement.executeUpdate(
					"INSERT INTO friend_ (time, senderUid, senderNickname, content) VALUES " + value);
		} catch (SQLException e) {
			logger.warn("error while saving friend message to SQLite: ", e);
			return false;
		}
		return true;
	}

	public boolean addQuote(Statement statement, LocalDateTime time, String speaker, String content) {
		try {
			String value =
					String.format("('%s', '%s', '%s')",
							time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " "),
							speaker,
							content);
			statement.executeUpdate("INSERT INTO quote_ (time, speaker, content) VALUES " + value);
		} catch (SQLException e) {
			logger.warn("error while saving quote to SQLite: ", e);
			return false;
		}
		return true;
	}

	@Override
	public void close() {
		RunningData.getInstance().set("groupId", String.valueOf(groupId));
		RunningData.getInstance().set("friendId", String.valueOf(friendId));
		RunningData.getInstance().save();
	}
}
