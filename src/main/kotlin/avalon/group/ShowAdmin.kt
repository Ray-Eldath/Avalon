package avalon.group

import avalon.api.Flag.AT
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.tool.pool.Constants.Basic.LANG
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object ShowAdmin : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val adminUid = groupConfig.admin
		val builder = StringBuilder("${AT(message)} ${LANG.getString("group.show_admin.reply")}\n")
		for (uid in adminUid) {
			val card = CURRENT_SERVLET.getGroupSenderNickname(message.groupUid, uid)
			if (!card.isEmpty())
				builder.append(card).append(" - ").append(uid).append(", ")
		}
		message.response(builder.toString().substring(0, builder.length - 2))
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("whoisadmin", LANG.getString("group.show_admin.help")),
					Pattern.compile("whoisadmin")
			)

	override fun instance(): GroupMessageResponder = this
}