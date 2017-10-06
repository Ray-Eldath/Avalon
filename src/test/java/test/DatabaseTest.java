package test;

import avalon.tool.database.SQLiteDatabaseOperator;
import avalon.tool.database.Table;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class DatabaseTest {
	public static void main(String[] args) {
		SQLiteDatabaseOperator.getInstance().add(new GroupMessage(
				2,
				System.currentTimeMillis(),
				123,
				"debug dd",
				12345,
				"asas",
				"al"));

		SQLiteDatabaseOperator.getInstance().add(new FriendMessage(
				1,
				System.currentTimeMillis(),
				123,
				"debug dd",
				"dddd"));

		SQLiteDatabaseOperator.getInstance().addQuote(
				123,
				"debug dd",
				"asd");

		System.out.println(SQLiteDatabaseOperator.getInstance().exist(Table.GROUP, "id=2"));
		System.out.println(SQLiteDatabaseOperator.getInstance().exist(Table.FRIEND, "senderUid=123"));
		System.out.println(SQLiteDatabaseOperator.getInstance().exist(Table.QUOTE, "speaker=\'debug dd\'"));
	}
}
