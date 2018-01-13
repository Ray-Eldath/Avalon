package avalon.tool.database;

import avalon.tool.system.RunningData;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import avalon.util.Message;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.*;
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

	private static PreparedStatement addGroupMessage, addFriendMessage, addQuote;

	BasicDatabaseOperator(Connection connection) {
		try {
			addGroupMessage = connection.prepareStatement(
					"INSERT INTO group_ (time, senderUid, senderNickName, groupUid, groupName, content) VALUES (?,?,?,?,?,?)");
			addFriendMessage = connection.prepareStatement(
					"INSERT INTO friend_ (time, senderUid, senderNickname, content) VALUES (?,?,?,?)");
			addQuote = connection.prepareStatement(
					"INSERT INTO quote_ (uid, speaker, content) VALUES (?,?,?)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean add(GroupMessage message) {
		try {
			addGroupMessage.clearParameters();

			addGroupMessage.setObject(1, time(message));
			addGroupMessage.setLong(2, message.getSenderUid());
			addGroupMessage.setString(3, UrlEncoded.encodeString(message.getSenderNickName()));
			addGroupMessage.setLong(4, message.getGroupUid());
			addGroupMessage.setString(5, UrlEncoded.encodeString(message.getGroupName()));
			addGroupMessage.setString(6, UrlEncoded.encodeString(message.getContent()));

			return addGroupMessage.execute();
		} catch (SQLException e) {
			logger.warn("error while saving group message to database: `" + e.getLocalizedMessage() + "`");
			return false;
		}
	}

	public boolean add(FriendMessage message) {
		try {
			addFriendMessage.clearParameters();

			addFriendMessage.setObject(1, time(message));
			addFriendMessage.setLong(2, message.getSenderUid());
			addFriendMessage.setString(3, UrlEncoded.encodeString(message.getSenderNickName()));
			addFriendMessage.setString(4, UrlEncoded.encodeString(message.getContent()));

			return addFriendMessage.execute();
		} catch (SQLException e) {
			logger.warn("error while saving friend message to SQLite: `" + e.getLocalizedMessage() + "`");
			return false;
		}
	}

	public boolean addQuote(int hashCode, String speaker, String content) {
		try {
			addQuote.clearParameters();

			addQuote.setInt(1, hashCode);
			addQuote.setString(2, UrlEncoded.encodeString(speaker));
			addQuote.setString(3, UrlEncoded.encodeString(content));

			return addQuote.execute();
		} catch (SQLException e) {
			logger.warn("error while saving quote to SQLite: `" + e.getLocalizedMessage() + "`");
			return false;
		}
	}

	public boolean exist(Statement statement, Table table, String condition) {
		try {
			ResultSet set = statement.executeQuery(String.format("SELECT COUNT(*) FROM %s WHERE %s", fromTable(table), condition));
			return set.getInt(1) != 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void close() {
		RunningData.getInstance().set("groupId", String.valueOf(groupId));
		RunningData.getInstance().set("friendId", String.valueOf(friendId));
		RunningData.getInstance().save();
	}

	private String time(Message message) {
		return message.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ");
	}

	private String fromTable(Table table) {
		if (table == Table.FRIEND)
			return "friend_";
		else if (table == Table.GROUP)
			return "group_";
		else if (table == Table.QUOTE)
			return "quote_";
		return null;
	}
}

