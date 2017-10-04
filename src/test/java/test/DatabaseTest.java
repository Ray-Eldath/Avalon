package test;

import avalon.tool.pool.Constants;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;

import java.time.LocalDateTime;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class DatabaseTest {
	public static void main(String[] args) {
		Constants.Database.currentDatabaseOperator.add(new GroupMessage(
				2,
				LocalDateTime.now(),
				123,
				"alpha",
				123,
				"beta",
				"test"));
		Constants.Database.currentDatabaseOperator.add(new FriendMessage(
				12,
				1233423,
				123,
				"sender",
				"test"));
	}
}
