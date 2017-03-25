package tool;

import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FriendMessage;
import util.GroupMessage;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

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
    public void closeResource() {
        try {
            statement.close();
        } catch (SQLException e) {
            logger.warn("Error while saving group message to SQLite: ", e);
        }
    }

    @Override
    public boolean addGroupMessage(GroupMessage input) {
        try {
            String value = "(" + input.getId() + ", '" +
                    input.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ") +
                    "', " + input.getSenderUid() + ", '" + input.getSenderNickName() +
                    "', " + input.getGroupUid() + ", '" + input.getGroupName() + "', '" +
                    UrlEncoded.encodeString(input.getContent()) + "')";
            statement.executeUpdate("INSERT INTO GroupMessage VALUES " + value);
        } catch (SQLException e) {
            logger.warn("Error while saving group message to SQLite: ", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean addFriendMessage(FriendMessage input) {
        try {
            String value = "(" + input.getId() + ", '" +
                    input.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ") +
                    "', " + input.getSenderUid() + ", '" + input.getSenderNickName() + "', '" +
                    UrlEncoded.encodeString(input.getContent()) + "')";
            statement.executeUpdate("INSERT INTO FriendMessage VALUES " + value);
        } catch (SQLException e) {
            logger.warn("Error while saving friend message to SQLite: ", e);
            return false;
        }
        return true;
    }
}
