package avalon.util.backend

import avalon.tool.pool.Constants
import avalon.tool.system.Configs
import avalon.util.FriendMessage
import avalon.util.GroupMessage
import org.eclipse.jetty.util.UrlEncoded
import org.json.JSONObject
import org.json.JSONTokener
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

object CoolQBackend : AvalonBackend() {
	private val groupMessageConsumer = Constants.groupMessageReceivedHook
	private val friendMessageConsumer = Constants.friendMessageReceivedHook

	private val `object` = Configs.Companion.instance().getJSONObject("backend")
	private val logger = LoggerFactory.getLogger(CoolQBackend::class.java)
	private var friendMessageId = 0
	private var groupMessageId = 0

	@Throws(IOException::class)
	override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
		req.characterEncoding = "UTF-8"
		resp.characterEncoding = "UTF-8"
		val jsonObject = JSONTokener(req.inputStream).nextValue() as JSONObject
		val postType = jsonObject.getString("post_type")
		if ("message" != postType) {
			if ("notice" == postType) {
				val noticeType = jsonObject.getString("notice_type")
				if ("group_decrease" == noticeType)
					if ("kick_me" != jsonObject.getString("sub_type"))
						GlobalGroupInfo.update()
				if ("group_increase" == noticeType)
					GlobalGroupInfo.update()
			}
			return
		}
		val messageType = jsonObject.getString("message_type")
		if ("private" == messageType && "friend" == jsonObject.getString("sub_type")) {
			val senderUid = jsonObject.getLong("user_id")
			friendMessageConsumer.accept(FriendMessage(
					friendMessageId++.toLong(),
					jsonObject.getLong("time"),
					senderUid,
					getFriendSenderNickname(senderUid),
					jsonObject.getString("message").replace("\\[CQ:\\S+]".toRegex(), "")
			))
		} else if ("group" == messageType) {
			if (!jsonObject.isNull("anonymous"))
				return
			val groupUid = jsonObject.getLong("group_id")
			val senderUid = jsonObject.getLong("user_id")
			groupMessageConsumer.accept(GroupMessage(
					groupMessageId++.toLong(),
					System.currentTimeMillis(),
					senderUid,
					getGroupSenderNickname(groupUid, senderUid),
					groupUid,
					handleNullableString(GlobalGroupInfo.queryGroup(groupUid)),
					jsonObject.getString("message").replace("\\[CQ:\\S+]".toRegex(), "")
			))
		}
	}

	private fun handleNullableString(input: String?): String = if (input === null) "ERROR - UNKNOWN" else input

	override fun responseGroup(groupUid: Long, reply: String) {
		val `object` = HashMap<String, Any>()
		`object`["group_id"] = groupUid
		`object`["message"] = reply
		`object`["auto_escape"] = !reply.contains("[CQ:")
		sendRequest("/send_group_msg", `object`)
	}

	override fun responseFriend(friendUid: Long, reply: String) = responsePrivate(friendUid, reply)

	override fun responsePrivate(uid: Long, reply: String) {
		val `object` = HashMap<String, Any>()
		`object`["user_id"] = uid
		`object`["message"] = reply
		`object`["auto_escape"] = !reply.contains("[CQ:")
		sendRequest("/send_private_msg", `object`)
	}

	override fun reboot() {
		sendRequest("/set_restart")
	}

	override fun shutUp(groupUid: Long, userUid: Long, time: Long) {
		val `object` = HashMap<String, Any>()
		`object`["group_id"] = groupUid
		`object`["user_id"] = userUid
		`object`["duration"] = time
		sendRequest("/set_group_ban", `object`)
	}

	override fun name(): String = "CoolQ"

	override fun apiAddress(): String = `object`.getString("api_address")

	override fun listenAddress(): String = `object`.getString("listen_address")

	override fun servlet(): Boolean = true

	override fun version(): String {
		return (JSONTokener(sendRequest("/get_version_info", null)).nextValue() as JSONObject)
				.getJSONObject("data").getString("plugin_version")
	}

	override fun getGroupSenderNickname(groupUid: Long, userUid: Long): String = handleNullableString(GlobalGroupInfo.queryGroupMember(groupUid, userUid))

	/*
	  实验性。还没改AvalonServlet。
	  获取群内所有成员的card。可添加最后发言时间筛选条件。
	 */
	fun getGroupMembersCards(groupUid: Long, filterLastSentTime: Long): List<String> {
		val `object` = HashMap<String, Any>()
		`object`["group_id"] = groupUid
		val object1 = (JSONTokener(sendRequest("/get_group_member_list", `object`)).nextValue() as JSONObject).getJSONArray("data")

		val result = ArrayList<String>()
		for (innerObject in object1) {
			val innerObject1 = innerObject as JSONObject
			if (innerObject1.getLong("last_sent_time") >= filterLastSentTime)
				result.add(innerObject1.getString("card"))
		}
		return result
	}

	override fun getFriendSenderNickname(uid: Long): String {
		val map = HashMap<String, Any>()
		map["user_id"] = uid
		return (JSONTokener(sendRequest("/get_stranger_info", map))
				.nextValue() as JSONObject).getJSONObject("data").getString("nickname")
	}

	private fun sendRequest(url: String, data: Map<String, Any>? = null): String {
		var nowUrl = url
		if (data == null)
			nowUrl = apiAddress() + url
		else {
			val requestBuilder = StringBuilder()
			val entrySet = data.entries
			for ((key, value) in entrySet) {
				requestBuilder.append(key).append("=")
				if (value is String)
					requestBuilder.append(UrlEncoded.encodeString(value))
				else
					requestBuilder.append(value.toString())
				requestBuilder.append("&")
			}
			val request = requestBuilder.toString()
			nowUrl = apiAddress() + nowUrl + "?" + request.substring(0, request.length - 1)
		}
		val response: String
		try {
			val connection = URL(nowUrl).openConnection()
			connection.readTimeout = 2000
			connection.connectTimeout = 2000
			val reader = BufferedReader(InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))
			response = reader.lineSequence().joinToString(separator = "")
			reader.close()
		} catch (e: Exception) {
			logger.error("exception thrown while sendRequest to " + url + " : `" + e.localizedMessage + "`")
			return ""
		}

		return response
	}

	object GlobalGroupInfo {
		private val groups = HashMap<Long, String>()
		private val groupMembers = HashMap<Long, HashMap<Long, String>>()

		init {
			update()
		}

		fun update() {
			val array = (JSONTokener(sendRequest("/get_group_list")).nextValue() as JSONObject).getJSONArray("data")
			var jsonObject: JSONObject
			for (obj in array) {
				jsonObject = obj as JSONObject
				groups[jsonObject.getNumber("group_id").toLong()] = jsonObject.getString("group_name")
			}

			for (groupNumber in groups.keys) {
				val toSent = HashMap<String, Any>()
				toSent["group_id"] = groupNumber
				val obj1 = (JSONTokener(sendRequest("/get_group_member_list", toSent)).nextValue() as JSONObject).getJSONArray("data")
				var jsonObject2: JSONObject
				val eachGroupMap = HashMap<Long, String>()
				for (eachGroup in obj1) {
					jsonObject2 = eachGroup as JSONObject
					val card = jsonObject2.getString("card")
					eachGroupMap[jsonObject2.getNumber("user_id").toLong()] =
							if (card.isEmpty())
								jsonObject2.getString("nickname")
							else card
				}
				groupMembers[groupNumber] = eachGroupMap
			}
		}

		fun queryGroup(groupId: Long): String? = groups[groupId]

		fun queryGroupMember(groupId: Long, memberId: Long): String? {
			val map = groupMembers[groupId]
			return if (map === null) null else map[memberId]
		}
	}
}
