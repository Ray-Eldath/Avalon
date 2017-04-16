package avalon.extend;

import avalon.group.MainGroupMessageHandler;
import avalon.tool.Response;

/**
 * Created by Eldath on 2017/1/31 0031.
 *
 * @author Eldath
 */
public class ShowMsgTask implements Task {
    private static String message;

    ShowMsgTask(String input) {
        ShowMsgTask.message = input;
    }

    @Override
    public void run() {
        long[] followGroups = MainGroupMessageHandler.getFollowGroup();
        for (long thisNeedShow : followGroups)
            Response.sendToGroup(thisNeedShow, message);
    }
}
