package avalon.api

import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.util.GroupMessage
import avalon.util.backend.CoolQBackend

object Flag {
	@JvmStatic
	fun AT(message: GroupMessage) =
			if (CURRENT_SERVLET is CoolQBackend)
				"[CQ:at,qq=${message.senderUid}]"
			else
				"@ ${message.senderNickName}"

	@JvmStatic
	fun AT(userUid: Long) =
			if (CURRENT_SERVLET is CoolQBackend)
				"[CQ:at,qq=$userUid]"
			else
				"@ $userUid"
}