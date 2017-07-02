package avalon.main;

import avalon.group.GroupMessageResponder;
import avalon.group.GroupMessageHandler;
import avalon.tool.pool.APISurvivePool;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class RegisterResponder {
    public static void register(GroupMessageResponder responder) {
        GroupMessageHandler.addGroupMessageResponder(responder);
        APISurvivePool.getInstance().addAPI(responder);
    }
}
