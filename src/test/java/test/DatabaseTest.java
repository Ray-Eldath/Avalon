package test;

import avalon.tool.database.H2DatabaseOperator;
import avalon.tool.database.Table;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;

/**
 * Created by Ray Eldath on 2017/4/19 0019.
 *
 * @author Ray Eldath
 */
public class DatabaseTest {
	public static void main(String[] args) {
		H2DatabaseOperator.INSTANCE.add(new GroupMessage(
				2,
				System.currentTimeMillis(),
				123,
				"DEBUG dd",
				12345,
				"asas",
				"al"));

		H2DatabaseOperator.INSTANCE.add(new FriendMessage(
				1,
				System.currentTimeMillis(),
				123,
				"DEBUG dd",
				"dddd"));

		H2DatabaseOperator.INSTANCE.addQuote(
				123,
				"DEBUG dd",
				"asd");

		System.out.println(H2DatabaseOperator.INSTANCE.exist(Table.GROUP, "id=2"));
		System.out.println(H2DatabaseOperator.INSTANCE.exist(Table.FRIEND, "senderUid=123"));
		System.out.println(H2DatabaseOperator.INSTANCE.exist(Table.QUOTE, "speaker=\'DEBUG+dd\'"));
		System.out.println(H2DatabaseOperator.INSTANCE.count(Table.GROUP));
		System.out.println(H2DatabaseOperator.INSTANCE.count(Table.FRIEND));
		System.out.println(H2DatabaseOperator.INSTANCE.count(Table.QUOTE));
	}
}
