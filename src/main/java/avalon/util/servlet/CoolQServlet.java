package avalon.util.servlet;

import avalon.tool.pool.Constants;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;


/**
 * Created by Eldath Ray on 2017/6/10 0010.
 *
 * @author Eldath Ray
 */
public class CoolQServlet extends AvalonServlet {
	private static final Logger logger = LoggerFactory.getLogger(CoolQServlet.class);
	private static int friendMessageId = 0;
	private static int groupMessageId = 0;
	private static Map<Long, String> groupIdToName = new HashMap<>();//TODO 最好写个定时更新
	private Consumer<GroupMessage> groupMessageConsumer;
	private Consumer<FriendMessage> friendMessageConsumer;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		JSONObject object = (JSONObject) (new JSONTokener(req.getInputStream()).nextValue());
		if (!"message".equals(object.getString("post_type")))
			return;
		String messageType = object.getString("message_type");
		if ("private".equals(messageType) && "friend".equals(object.getString("sub_type"))) {
			long senderUid = object.getLong("user_id");
			friendMessageConsumer.accept(new FriendMessage(
					friendMessageId++,
					object.getLong("time"),
					senderUid,
					getFriendSenderNickname(senderUid),
					object.getString("message").replaceAll("\\[CQ:\\w*", "")
			));
		} else if ("group".equals(messageType)) {
			if (!object.getString("anonymous").isEmpty())
				return;
			long groupUid = object.getLong("group_id");
			long senderUid = object.getLong("user_id");
			groupMessageConsumer.accept(new GroupMessage(
					groupMessageId++,
					System.currentTimeMillis(),
					senderUid,
					getGroupSenderNickname(groupUid, senderUid),
					groupUid,
					getGroupName(groupUid),
					object.getString("message").replaceAll("\\[CQ:\\w*", "")
			));
		}
	}

	private String getGroupName(long groupUid) {
		if (groupIdToName.containsKey(groupUid))
			return groupIdToName.get(groupUid);
		JSONArray array = ((JSONObject) new JSONTokener(sendRequest("/get_group_list", null))
				.nextValue()).getJSONArray("data");
		String r;
		JSONObject object;
		for (Object obj : array) {
			object = (JSONObject) obj;
			if (object.getLong("group_id") == groupUid) {
				r = object.getString("group_name");
				groupIdToName.put(groupUid, r);
				return r;
			}
		}
		return "ERROR - UNKNOWN";
	}

	@Override
	public void responseGroup(long groupUid, String reply) {
		if (Constants.Basic.debug || Constants.Basic.localOutput) {
			System.out.println("Group output: " + reply);
			return;
		}
		Map<String, Object> object = new HashMap<>();
		object.put("group_id", groupUid);
		object.put("message", reply);
		object.put("is_raw", !reply.contains("[CQ:"));
		sendRequest("/send_group_msg", object);
	}

	@Override
	public void responseFriend(long friendUid, String reply) {
		if (Constants.Basic.debug || Constants.Basic.localOutput) {
			System.out.println("Friend output: " + reply);
			return;
		}
		Map<String, Object> object = new HashMap<>();
		object.put("user_id", friendUid);
		object.put("message", reply);
		object.put("is_raw", !reply.contains("[CQ:"));
		sendRequest("/send_private_msg", object);
	}

	@Override
	public void shutUp(long groupUid, long userUid, long time) {
		Map<String, Object> object = new HashMap<>();
		object.put("group_id", groupUid);
		object.put("user_id", userUid);
		object.put("duration", time);
		sendRequest("/set_group_ban", object);
	}

	@Override
	public void shutdown() {
		throw new UnsupportedOperationException("please shutdown manually");
	}

	@Override
	public String name() {
		return "CoolQ";
	}

	@Override
	public String getGroupSenderNickname(long groupUid, long userUid) {
		Map<String, Object> object = new HashMap<>();
		object.put("group_id", groupUid);
		object.put("user_id", userUid);
		JSONObject object1 = (JSONObject) (new JSONTokener(sendRequest("/get_group_member_info", object)).nextValue());
		return object1.getInt("retcode") != 0 ? "" : object1.getJSONObject("data").getString("card");
	}

	@Override
	public String getFriendSenderNickname(long uid) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("user_id", uid);
		return ((JSONObject) (new JSONTokener(sendRequest("/get_stranger_info", map))
				.nextValue())).getJSONObject("data").getString("nickname");
	}

	@Override
	public void setGroupMessageReceivedHook(Consumer<GroupMessage> hook) {
		this.groupMessageConsumer = hook;
	}

	@Override
	public void setFriendMessageReceivedHook(Consumer<FriendMessage> hook) {
		this.friendMessageConsumer = hook;
	}

	private String sendRequest(String url, Map<String, Object> data) {
		if (data == null)
			url = apiAddress() + url;
		else {
			StringBuilder requestBuilder = new StringBuilder();
			Set<Map.Entry<String, Object>> entrySet = data.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				requestBuilder.append(key).append("=");
				if (value instanceof String)
					requestBuilder.append(UrlEncoded.encodeString((String) value));
				else
					requestBuilder.append(String.valueOf(value));
				requestBuilder.append("&");
			}
			String request = requestBuilder.toString();
			url = apiAddress() + url + "?" + request.substring(0, request.length() - 1);
		}
		StringBuilder response = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String thisLine;
			while ((thisLine = reader.readLine()) != null)
				response.append(thisLine);
			reader.close();
		} catch (Exception e) {
			logger.error("exception thrown while sendRequest to " + url + " " + e);
		}
		return response.toString();
	}
}
