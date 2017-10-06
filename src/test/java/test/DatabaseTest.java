package test;

import avalon.tool.database.SQLiteDatabaseOperator;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class DatabaseTest {
	public static void main(String[] args) {
		SQLiteDatabaseOperator.getInstance().addQuote(123, "123", "123");
		SQLiteDatabaseOperator.getInstance().exist("quote_", "uid=123");
	}
}
