package avalon.util.servlet;

import avalon.tool.system.ConfigSystem;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by Eldath Ray on 2017/6/9 0009.
 *
 * @author Eldath Ray
 */
public abstract class AvalonServlet extends HttpServlet {
	private static JSONObject object = ConfigSystem.getInstance().getJSONObject("servlet");

	abstract public String name();

	public String version() {
		return "UNKNOWN";
	}

	public String scriptFilePath() {
		return "";
	}

	public String apiAddress() {
		return object.getString("api_address");
	}

	public String listenAddress() {
		return object.getString("listen_address");
	}

	abstract public void responseGroup(long groupUid, String reply);

	abstract public void responseFriend(long friendUid, String reply);

	abstract public void shutUp(long groupUid, long userUid, long time);

	abstract public void shutdown();

	public void clean() {
		System.out.println("No clean needed.");
	}

	abstract public String getGroupSenderNickname(long groupUid, long userUid);

	abstract public String getFriendSenderNickname(long uid);

	abstract public void setGroupMessageReceivedHook(Consumer<GroupMessage> hook);

	abstract public void setFriendMessageReceivedHook(Consumer<FriendMessage> hook);

	@Override
	abstract public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
