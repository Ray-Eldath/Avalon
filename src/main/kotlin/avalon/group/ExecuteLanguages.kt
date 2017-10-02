package avalon.group

import avalon.tool.Executives
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object ExecuteLanguages : GroupMessageResponder {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val list = Executives.EXECUTIVE.allLanguages()
		message.response("`avalon execute`指令支持的语言有：${list.joinToString()}")
	}

	override fun getHelpMessage(): String? = null

	override fun getKeyWordRegex(): Pattern = Pattern.compile("avalon execute languages")

	override fun instance(): GroupMessageResponder = this
}