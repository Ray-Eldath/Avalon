package avalon.tool;

import avalon.util.FriendMessage;
import avalon.util.GroupMessage;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public interface DatabaseOperator {
    boolean addGroupMessage(GroupMessage input);

    boolean addFriendMessage(FriendMessage input);

    void closeResource();
}
