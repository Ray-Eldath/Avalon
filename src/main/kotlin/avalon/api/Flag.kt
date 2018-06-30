package avalon.api

import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.util.GroupMessage
import avalon.util.backend.CoolQBackend
import avalon.util.backend.DiscordBackend

object Flag {
	@JvmStatic
	fun at(message: GroupMessage) =
			when (CURRENT_SERVLET) {
				is CoolQBackend -> "[CQ:at,qq=${message.senderUid}]"
				DiscordBackend -> DiscordBackend.jda.getUserById(message.senderUid).asMention
				else -> "@${message.senderNickName}"
			}!!

	@JvmStatic
	fun at(userUid: Long) =
			when (CURRENT_SERVLET) {
				is CoolQBackend -> "[CQ:at,qq=$userUid]"
				DiscordBackend -> DiscordBackend.jda.getUserById(userUid).asMention
				else -> "@$userUid"
			}!!
}