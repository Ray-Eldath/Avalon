package test.extend;

import avalon.tool.database.SQLiteDatabaseOperator;
import avalon.util.GroupMessage;

import java.time.LocalDateTime;

/**
 * Created by Eldath Ray on 2017/6/8 0008.
 *
 * @author Eldath Ray
 */
public class RecorderTest {
    public static void main(String[] args) {
        SQLiteDatabaseOperator.getInstance().add(new GroupMessage(0, LocalDateTime.now(), 1,
                "1", 2, "2", "TT"));
    }
}