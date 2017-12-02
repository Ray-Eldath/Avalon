package avalon.group

import avalon.api.Flag.AT
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object ShowAdmin : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val adminUid = groupConfig.admin
		val builder = StringBuilder(AT(message) + " 目前管理员有：\n")
		for (uid in adminUid) {
			val card = CURRENT_SERVLET.getGroupSenderNickname(message.groupUid, uid)
			if (!(card == null || card.isEmpty()))
				builder.append(card).append(" - ").append(uid).append(", ")
		}
		message.response(builder.toString().substring(0, builder.length - 2))
	}

	override fun getHelpMessage() = "avalon (whoisadmin|谁是管理员)：显示管理员列表"

	override fun getKeyWordRegex(): Pattern = Pattern.compile("^avalon (whoisadmin|谁是管理员)")

	override fun instance(): GroupMessageResponder = this
}