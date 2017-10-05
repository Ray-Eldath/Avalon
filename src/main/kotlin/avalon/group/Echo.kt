package avalon.group

import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
object Echo : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val content = message.content.replace("avalon echo ", "").replace("avalon repeat ", "").replace("阿瓦隆跟我说 ", "")
		message.response(content)
	}

	override fun getHelpMessage(): String = "avalon (echo|repeat)|阿瓦隆跟我说：让阿瓦隆重复给定语句"

	override fun getKeyWordRegex(): Pattern = Pattern.compile("^avalon (echo|repeat)|^阿瓦隆跟我说 ")
}