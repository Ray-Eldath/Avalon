package avalon.group

import avalon.api.Flag.AT
import avalon.tool.database.Table
import avalon.tool.pool.Constants.Basic.LANG
import avalon.tool.pool.Constants.Database.CURRENT_DATABASE_OPERATOR
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Quote : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val hashCode = message.hashCode()
		if (CURRENT_DATABASE_OPERATOR.exist(Table.QUOTE, "uid=$hashCode")) {
			message.response("${AT(message)} ${LANG.getString("group.quote.recorded")}")
			return
		}
		val split = message.content.split(" ") // avalon quote 123 456
		val speaker = split[2]
		val content = split[3]
		CURRENT_DATABASE_OPERATOR.addQuote(hashCode, speaker, content)
		message.response("${AT(message)} ${LANG.getString("group.quote.success")}")
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("quote <发言者> <语录内容>", "记录语录到Avalon数据库。"),
					Pattern.compile("quote \\S+ \\S+"),
					permission = ResponderPermission.ADMIN
			)

	override fun instance(): GroupMessageResponder? = this
}