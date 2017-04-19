package avalon.tool;

import avalon.api.util.FriendMessage;
import avalon.api.util.GroupMessage;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class MySQLDatabaseOperator implements DatabaseOperator {
    @Override
    public boolean addGroupMessage(GroupMessage input) {
        return false;
    }

    @Override
    public boolean addFriendMessage(FriendMessage input) {
        return false;
    }

    @Override
    public void closeResource() {

    }
}
