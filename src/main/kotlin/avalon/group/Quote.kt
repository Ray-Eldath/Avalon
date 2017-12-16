package avalon.group

import avalon.api.Flag.AT
import avalon.tool.database.Table
import avalon.tool.pool.Constants.Database.CURRENT_DATABASE_OPERATOR
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Quote : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val hashCode = message.hashCode()
		if (CURRENT_DATABASE_OPERATOR.exist(Table.QUOTE, "uid=$hashCode")) {
			message.response("${AT(message)} 给定的Quote已经记录过啦~（　^ω^）")
			return
		}
		val split = message.content.replace("avalon quote ", "").split(" ")
		val speaker = split[0]
		val content = split[1]
		CURRENT_DATABASE_OPERATOR.addQuote(hashCode, speaker, content)
		message.response("${AT(message)} 给定的语录已添加至数据库~")
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("avalon quote <发言者> <语录内容>", "记录语录到Avalon数据库。"),
					Pattern.compile("^avalon quote \\S+ \\S+"),
					permission = ResponderPermission.ADMIN
			)

	override fun instance(): GroupMessageResponder? = this
}