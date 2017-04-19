package test;

import avalon.api.util.FriendMessage;
import avalon.api.util.GroupMessage;
import avalon.tool.ConstantPool;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class DatabaseTest {
    public static void main(String[] args) {
        ConstantPool.Database.currentDatabaseOperator.add(new GroupMessage(
                2, 12345, 123, "alpha", 123, "beta", "test"));
        ConstantPool.Database.currentDatabaseOperator.add(new FriendMessage(12, 1233423, 123, "sender", "test"));
    }
}
