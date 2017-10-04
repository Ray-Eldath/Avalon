package avalon.api

import avalon.tool.pool.Constants.Basic.currentServlet
import avalon.util.GroupMessage
import avalon.util.servlet.CoolQServlet

object Flag {
	@JvmStatic
	fun AT(message: GroupMessage): String =
			if (currentServlet is CoolQServlet)
				"[CQ:at,qq=${message.senderUid}]"
			else
				"@ ${message.senderNickName}"

	@JvmStatic
	fun AT(userUid: Long): String =
			if (currentServlet is CoolQServlet)
				"[CQ:at,qq=$userUid]"
			else
				"@ $userUid"
}