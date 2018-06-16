package avalon.group

import avalon.tool.Executives
import avalon.tool.pool.Constants.Basic.LANG
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object ExecuteInfo : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val list = Executives.EXECUTIVE.allLanguages()
		message.response(LANG.getString("group.execute_info.reply").format(Executives.EXECUTIVE.name(), list.joinToString()))
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("", ""),
					Pattern.compile("(exi|execute info)")
			)

	override fun instance() = this
}