package avalon.util.servlet;

import avalon.tool.system.Configs;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static JSONObject object = Configs.INSTANCE.getJSONObject("backend");
	private static Logger logger = LoggerFactory.getLogger(AvalonServlet.class);

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

	public boolean test() {
		try {
			return version() != null;
		} catch (Exception e) {
			return false;
		}
	}

	abstract public void responseGroup(long groupUid, @NotNull String reply);

	abstract public void responseFriend(long friendUid, @NotNull String reply);

	abstract public void responsePrivate(long uid, @NotNull String reply);

	abstract public void shutUp(long groupUid, long userUid, long time);

	public void shutdown() {
		logger.info("please shutdown " + name() + " service manually");
	}

	public void reboot() {
		logger.info("please reboot " + name() + " service manually");
	}

	public void clean() {
		logger.info("No clean needed.");
	}

	abstract public String getGroupSenderNickname(long groupUid, long userUid);

	abstract public String getFriendSenderNickname(long uid);

	abstract public void setGroupMessageReceivedHook(@NotNull Consumer<GroupMessage> hook);

	abstract public void setFriendMessageReceivedHook(@NotNull Consumer<FriendMessage> hook);

	abstract public void doPost(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp) throws ServletException, IOException;
}
