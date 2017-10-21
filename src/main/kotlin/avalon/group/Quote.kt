package avalon.group

import avalon.api.Flag.AT
import avalon.tool.database.Table
import avalon.tool.pool.Constants.Database.currentDatabaseOperator
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Quote : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val hashCode = message.hashCode()
		if (currentDatabaseOperator.exist(Table.QUOTE, "uid=$hashCode")) {
			message.response("${AT(message)} 给定的Quote已经记录过啦~（　^ω^）")
			return
		}
		val split = message.content.replace("avalon quote ", "").split(" ")
		val speaker = split[0]
		val content = split[1]
		currentDatabaseOperator.addQuote(hashCode, speaker, content)
		message.response("${AT(message)} 给定的语录已添加至数据库~")
	}

	override fun permission(): ResponderPermission = ResponderPermission.ADMIN

	override fun getHelpMessage(): String = "avalon quote <发言者> <语录内容>：<管理员> 记录语录到Avalon数据库。"

	override fun getKeyWordRegex(): Pattern = Pattern.compile("^avalon quote \\S+ \\S+")

	override fun instance(): GroupMessageResponder? = this
}