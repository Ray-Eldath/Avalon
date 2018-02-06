package avalon.api

import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.util.GroupMessage
import avalon.util.backend.CoolQBackend
import avalon.util.backend.DiscordBackend

object Flag {
	@JvmStatic
	fun AT(message: GroupMessage) =
			when (CURRENT_SERVLET) {
				is CoolQBackend -> "[CQ:at,qq=${message.senderUid}]"
				DiscordBackend -> DiscordBackend.jda.getUserById(message.senderUid).asMention
				else -> "@${message.senderNickName}"
			}

	@JvmStatic
	fun AT(userUid: Long) =
			if (CURRENT_SERVLET is CoolQBackend)
				"[CQ:at,qq=$userUid]"
			else
				"@$userUid"
}