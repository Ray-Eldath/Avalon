package avalon.tool.database;

import avalon.api.util.FriendMessage;
import avalon.api.util.GroupMessage;
import avalon.tool.RunningData;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class BasicDatabaseOperator implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(BasicDatabaseOperator.class);
    private static int groupId = Integer.parseInt(RunningData.getInstance().get("groupId"));
    private static int friendId = Integer.parseInt(RunningData.getInstance().get("friendId"));

    private static final BasicDatabaseOperator instance = new BasicDatabaseOperator();

    public static BasicDatabaseOperator getInstance() {
        return instance;
    }

    private BasicDatabaseOperator() {
    }

    public boolean add(Statement statement, GroupMessage message) {
        try {
            String value = "('" + message.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ") +
                    "', " + message.getSenderUid() + ", '" + message.getSenderNickName() +
                    "', " + message.getGroupUid() + ", '" + message.getGroupName() + "', '" +
                    UrlEncoded.encodeString(message.getContent()) + "')";
            statement.executeUpdate("INSERT INTO group_ VALUES " + value);
        } catch (SQLException e) {
            logger.warn("Error while saving group message to database: ", e);
            return false;
        }
        return true;
    }

    public boolean add(Statement statement, FriendMessage message) {
        try {
            String value = "('" + message.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ") +
                    "', " + message.getSenderUid() + ", '" + message.getSenderNickName() + "', '" +
                    UrlEncoded.encodeString(message.getContent()) + "')";
            statement.executeUpdate("INSERT INTO friend_ VALUES " + value);
        } catch (SQLException e) {
            logger.warn("Error while saving friend message to SQLite: ", e);
            return false;
        }
        return true;
    }

    public void close() {
        RunningData.getInstance().set("groupId", String.valueOf(groupId));
        RunningData.getInstance().set("friendId", String.valueOf(friendId));
        RunningData.getInstance().save();
    }
}
