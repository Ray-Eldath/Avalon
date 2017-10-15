package avalon.main

import avalon.friend.FriendMessageHandler
import avalon.group.GroupMessageHandler
import avalon.util.FriendMessage
import avalon.util.GroupMessage
import org.json.JSONObject
import org.json.JSONTokener
import org.slf4j.LoggerFactory
import javax.servlet.http.*

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
@Deprecated("replaced by {@link avalon.util.servlet.AvalonServlet#setGroupMessageReceivedHook(Consumer)}")
class MainServlet : HttpServlet() {

	public override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
		req.characterEncoding = "UTF-8"
		resp.characterEncoding = "UTF-8"
		val obj = JSONTokener(req.reader).nextValue() as JSONObject
		if (obj.isNull("post_type") || obj.isNull("type")) return
		if ("receive_message" != obj.getString("post_type"))
			return
		//
		val timeLong = obj.getLong("time")
		val id = obj.getInt("id")
		val senderUid = if (obj.isNull("sender_uid")) {
			logger.error("\"sender_uid\" is null! \n " +
					"That means the your Mojo-Webqq is outdated or " +
					"Tencent delete some API. If this problem still exist, " +
					"try update Mojo-Webqq or new issues at:" +
					" https://github.com/sjdy521/Mojo-Webqq/issues.")
			1234567890
		} else
			obj.getLong("sender_uid")
		val sender = obj["sender"].toString()
		val content = obj["content"].toString()
		val type = obj.getString("type")
		if (!("friend_message" == type || "group_message" == type))
			return
		if ("friend_message" == type) {
			val message = FriendMessage(id, timeLong, senderUid, sender, content)
			if (!MessageChecker.check(message)) return
			FriendMessageHandler.handle(message)
		} else {
			val groupUid = if (obj.isNull("group_uid")) {
				logger.error("\"group_uid\" is null! \n " +
						"That means the your Mojo-Webqq is outdated or " +
						"Tencent delete some API. If this problem still exist, " +
						"try update Mojo-Webqq or new issues at:" +
						" https://github.com/sjdy521/Mojo-Webqq/issues.")
				1234567891L
			} else
				obj.getLong("sender_uid")
			val group = obj["group"].toString()
			GroupMessageHandler.getInstance().handle(GroupMessage(id, timeLong,
					senderUid, sender, groupUid, group, content))
		}
	}

	companion object {
		private val logger = LoggerFactory.getLogger(MainServlet::class.java)
	}
}