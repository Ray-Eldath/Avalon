package avalon.util.servlet;

import avalon.main.MessageChecker;
import avalon.tool.pool.Constants;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.function.Consumer;

import static avalon.tool.pool.Constants.Address.servlet;

/**
 * Created by Eldath Ray on 2017/6/9 0009.
 *
 * @author Eldath Ray
 */
public class MojoWebqqServlet extends AvalonServlet {
	private static final Logger logger = LoggerFactory.getLogger(MojoWebqqServlet.class);
	private Consumer<GroupMessage> groupMessageConsumer;
	private Consumer<FriendMessage> friendMessageConsumer;
	private String version = "UNKNOWN";

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		JSONObject object = (JSONObject) new JSONTokener(req.getReader()).nextValue();
		if (object.isNull("post_type") || object.isNull("type")) return;
		if (!"receive_message".equals(object.getString("post_type")))
			return;
		//
		long timeLong = object.getLong("time");
		int Id = object.getInt("id");
		long senderUid;
		if (object.isNull("sender_uid")) {
			logger.error("\"sender_uid\" is null! \n " +
					"That means the your Mojo-Webqq is outdated or " +
					"Tencent delete some API. If this problem still exist, " +
					"try update Mojo-Webqq or new issues at:" +
					" https://github.com/sjdy521/Mojo-Webqq/issues.");
			senderUid = 1234567890;
		} else senderUid = object.getLong("sender_uid");
		String sender = object.get("sender").toString();
		String content = object.get("content").toString();
		String type = object.getString("type");
		if (!("friend_message".equals(type) || "group_message".equals(type)))
			return;
		if ("friend_message".equals(type)) {
			FriendMessage message = new FriendMessage(Id, timeLong, senderUid, sender, content);
			if (!MessageChecker.check(message)) return;
			friendMessageConsumer.accept(message);
		} else {
			long groupUid;
			if (object.isNull("group_uid")) {
				logger.error("\"group_uid\" is null! \n " +
						"That means the your Mojo-Webqq is outdated or " +
						"Tencent delete some API. If this problem still exist, " +
						"try update Mojo-Webqq or new issues at:" +
						" https://github.com/sjdy521/Mojo-Webqq/issues.");
				groupUid = 1234567891;
			} else groupUid = object.getLong("sender_uid");
			String group = object.get("group").toString();
			groupMessageConsumer.accept(new GroupMessage(Id, timeLong,
					senderUid, sender, groupUid, group, content));
		}
	}

	@Override
	public String version() {
		if ("UNKNOWN".equals(version))
			try {
				version = ((JSONObject) new JSONTokener(
						new URL(Constants.Address.servlet + "/openqq/get_client_info")
								.openStream()).nextValue()).getString("version");
			} catch (IOException ignore) {
			}
		return version;
	}

	@Override
	public String scriptFilePath() {
		return Constants.Basic.currentPath + File.separator + "bin" + File.separator + "Mojo-Webqq.pl";
	}

	@Override
	public void shutdown() {
		try {
			new URL(servlet + "/openqq/stop_client").openStream();
		} catch (IOException ignored) {
		}
	}

	@Override
	public void clean() {
		File[] files = new File(System.getProperty("java.io.tmpdir")).listFiles();
		if (files != null)
			Arrays.stream(files).filter(e -> e.getName().trim().matches("mojo_")).forEach(File::delete);
		logger.info("Mojo-Webqq files and Mojo-Weixin files cleaned.");
	}

	@Override
	public void responseGroup(long groupUid, String reply) {
		if (Constants.Basic.localOutput)
			System.out.println("Group output: " + reply);
		else
			try {
				new URL(servlet + "/openqq/send_group_message?uid=" + groupUid + "&content=" +
						UrlEncoded.encodeString(reply)).openStream();
			} catch (IOException e) {
				logger.warn("IOException thrown while response avalon.group message: " + e.toString());
			}
	}

	@Override
	public void responseFriend(long friendUid, String reply) {
		if (Constants.Basic.localOutput)
			System.out.println("Friend output: " + reply);
		else
			try {
				new URL(servlet + "/openqq/send_friend_message?uid=" + friendUid + "&content=" +
						UrlEncoded.encodeString(reply)).openStream();
			} catch (IOException e) {
				logger.warn("IOException thrown while response friend message: " + e.toString());
			}
	}

	@Override
	public void responsePrivate(long uid, String reply) {
		throw new UnsupportedOperationException("response private message unsupported yet");
	}

	@Override
	public void shutUp(long groupUid, long userUid, long time) {
		try {
			new URL(servlet + "/openqq/shutup_group_member?time=" + time +
					"&group_uid=" + groupUid +
					"&member_uid=" + userUid).openStream();
			logger.info(String.format("UID %d in group UID %d has been shut up for %d seconds.",
					userUid, groupUid, time));
		} catch (IOException e) {
			logger.warn("IOException thrown while response group message and shut up: " + e.toString());
		}
	}

	@Override
	public String name() {
		return "Mojo-Webqq";
	}

	@Override
	public String getGroupSenderNickname(long groupUid, long userUid) {
		return null;
	}

	@Override
	public String getFriendSenderNickname(long uid) {
		return null;
	}

	@Override
	public void setGroupMessageReceivedHook(Consumer<GroupMessage> hook) {
		this.groupMessageConsumer = hook;
	}

	@Override
	public void setFriendMessageReceivedHook(Consumer<FriendMessage> hook) {
		this.friendMessageConsumer = hook;
	}

	private static String addressHandle(String address) {
		return address.endsWith("/") ? address.substring(0, address.length() - 1) : address;
	}
}
