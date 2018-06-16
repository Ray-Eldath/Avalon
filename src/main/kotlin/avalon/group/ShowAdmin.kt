package avalon.group

import avalon.api.Flag.AT
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.tool.pool.Constants.Basic.LANG
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object ShowAdmin : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val adminUid = groupConfig.admins
		val builder = StringBuilder()
		for (uid in adminUid) {
			val card = CURRENT_SERVLET.getGroupSenderNickname(message.groupUid, uid)
			if (!card.isEmpty())
				builder.append(card).append(" - ").append(uid).append(", ")
		}
		if (builder.isEmpty()) {
			message.response("${AT(message)} ${LANG.getString("group.show_admin.no_admin")}")
			return
		}
		val toDisplay = LANG.getString("group.show_admin.reply")
				.format("\n" + builder.toString().substring(0, builder.length - 2))
		message.response("${AT(message)} $toDisplay")
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("(wia|whoisadmin)", LANG.getString("group.show_admin.help")),
					Pattern.compile("(wia|whoisadmin)")
			)

	override fun instance(): GroupMessageResponder = this
}