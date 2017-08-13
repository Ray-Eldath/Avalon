package avalon.tool;

import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Eldath Ray on 2017/3/25 0025.
 *
 * @author Eldath Ray
 */
public class ShowAllMessage {
	private static Logger logger = LoggerFactory.getLogger(ShowAllMessage.class);
	private static Statement statement;

	public static void main(String[] args) throws Exception {
		try {
			Class.forName("org.sqlite.JDBC");
			statement = DriverManager.getConnection("jdbc:sqlite:res/record.db").createStatement();
		} catch (Exception e) {
			logger.error("Fatal error while load SQLite driver: ", e);
			System.exit(-1);
		}
		Scanner scanner = new Scanner(System.in);
		logger.info("What kind of message do you want to show? (all/group/friend): ");
		String kind = scanner.nextLine().toLowerCase();
		if ("".equals(kind))
			error();
		if ("all".equals(kind)) {
			showFriendMessage();
			showGroupMessage();
		} else if ("group".equals(kind))
			showGroupMessage();
		else if ("friend".contains(kind))
			showFriendMessage();
		else error();
	}

	private static void error() {
		System.err.println("Error: invalid input!");
		System.exit(-16885);
	}

	private static void showFriendMessage() throws SQLException {
		List<FriendMessage> friendMessages = new ArrayList<>();
		ResultSet resultSet = statement.executeQuery("SELECT * from friend_ ORDER BY time ASC;");
		while (resultSet.next()) {
			friendMessages.add(new FriendMessage(resultSet.getInt("id"),
					LocalDateTime.parse(resultSet.getString("time").replace(" ", "T")),
					resultSet.getLong("senderUid"),
					resultSet.getString("senderNickName"),
					UrlEncoded.decodeString(resultSet.getString("content"))));
		}
		for (FriendMessage thisFriendMessage : friendMessages)
			System.out.println(thisFriendMessage.getString());
	}

	private static void showGroupMessage() throws SQLException {
		List<GroupMessage> groupMessages = new ArrayList<>();
		ResultSet resultSet = statement.executeQuery("SELECT * from group_ ORDER BY time ASC;");
		while (resultSet.next()) {
			groupMessages.add(new GroupMessage(resultSet.getInt("id"),
					LocalDateTime.parse(resultSet.getString("time").replace(" ", "T")),
					resultSet.getLong("senderUid"),
					resultSet.getString("senderNickName"),
					resultSet.getLong("groupUid"),
					resultSet.getString("groupName"),
					UrlEncoded.decodeString(resultSet.getString("content"))));
		}
		for (GroupMessage thisGroupMessage : groupMessages)
			System.out.println(String.format("%s", thisGroupMessage.getString()));
	}
}
