package avalon.function.task;

import avalon.tool.system.GroupConfigs;
import org.slf4j.LoggerFactory;

import java.util.List;

import static avalon.tool.pool.Constants.Basic.INSTANCE;

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
		List<Long> followGroups = GroupConfigs.instance().getFollowGroups();
		for (long thisNeedShow : followGroups)
			INSTANCE.getCURRENT_SERVLET().responseGroup(thisNeedShow, message);
	}

	@Override
	public String toString() {
		return "ShowMsgTask{" + message + "}";
	}
}
