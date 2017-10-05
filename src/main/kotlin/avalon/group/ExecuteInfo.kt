package avalon.group

import avalon.tool.Executives
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object ExecuteInfo : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val list = Executives.EXECUTIVE.allLanguages()
		message.response(
				"`avalon execute`由${Executives.EXECUTIVE.name()}提供代码执行器服务，支持的语言有：${list.joinToString()}")
	}

	override fun getHelpMessage(): String? = null

	override fun getKeyWordRegex(): Pattern = Pattern.compile("avalon execute info")

	override fun instance(): GroupMessageResponder = this
}