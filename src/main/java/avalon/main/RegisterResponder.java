package avalon.main;

import avalon.group.BaseGroupMessageResponder;
import avalon.group.MainGroupMessageHandler;
import avalon.tool.pool.APISurvivePool;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class RegisterResponder {
    public static void register(BaseGroupMessageResponder responder) {
        MainGroupMessageHandler.addGroupMessageResponder(responder);
        APISurvivePool.getInstance().addAPI(responder);
    }
}
