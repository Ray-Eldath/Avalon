package avalon.extend.task;

import avalon.tool.system.GroupConfig;
import org.slf4j.LoggerFactory;

import java.util.List;

import static avalon.tool.pool.Constants.Basic.CURRENT_SERVLET;

/**
 * Created by Eldath on 2017/1/31 0031.
 *
 * @author Eldath
 */
public class ShowMsgTask implements Task {
	private String message;

	public ShowMsgTask(String input) {
		this.message = input;
	}

	@Override
	public void run() {
		LoggerFactory.getLogger(ShowMsgTask.class).info("echo message \"" +
				message.replace("\n", "") + "\" to every group.");
		List<Long> followGroups = GroupConfig.instance().getFollowGroups();
		for (long thisNeedShow : followGroups)
			CURRENT_SERVLET.responseGroup(thisNeedShow, message);
	}

	@Override
	public String toString() {
		return "ShowMsgTask{" + message + "}";
	}
}
