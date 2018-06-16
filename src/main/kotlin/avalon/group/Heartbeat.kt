package avalon.group

import avalon.api.Flag.AT
import avalon.tool.pool.Constants.Basic.LANG
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

/**
 * @since v1.2.4
 */
object Heartbeat : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val datetime = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME)
		val reply = String.format("%s %s", AT(message), String.format(LANG.getString("group.heartbeat.reply"), datetime))
		message.response(reply)
	}

	override fun instance(): GroupMessageResponder? = this

	override fun responderInfo(): ResponderInfo = ResponderInfo(
			helpMessage = Pair("(hb|heartbeat)", LANG.getString("group.heartbeat.help")),
			keyWordRegex = Pattern.compile("(hb|heartbeat)"),
			manageable = false)
}