package avalon.util.backend;

import avalon.tool.system.Configs;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import avalon.util.Service;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Created by Eldath Ray on 2017/6/9 0009.
 *
 * @author Eldath Ray
 */
public abstract class AvalonBackend extends HttpServlet implements Service {
	private static JSONObject object = Configs.INSTANCE.getJSONObject("backend");
	private static Logger logger = LoggerFactory.getLogger(AvalonBackend.class);

	abstract public String name();

	@NotNull
	public String version() {
		return "UNKNOWN";
	}

	@NotNull
	public String scriptFilePath() {
		return "";
	}

	@NotNull
	public String apiAddress() {
		return object.getString("api_address");
	}

	@NotNull
	public String listenAddress() {
		return object.getString("listen_address");
	}

	@Override
	public boolean available() {
		try {
			new URL(apiAddress()).openConnection().connect();
			return true;
		} catch (Exception e) {
			logger.error("exception thrown while testing usability of backend: `" + e.getLocalizedMessage() + "`");
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

	@NotNull
	abstract public String getGroupSenderNickname(long groupUid, long userUid);

	@NotNull
	abstract public String getFriendSenderNickname(long uid);

	abstract public void setGroupMessageReceivedHook(@NotNull Consumer<GroupMessage> hook);

	abstract public void setFriendMessageReceivedHook(@NotNull Consumer<FriendMessage> hook);

	abstract public void doPost(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp) throws ServletException, IOException;
}
