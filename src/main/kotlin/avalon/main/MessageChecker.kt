package avalon.main

import avalon.api.Flag.AT
import avalon.tool.pool.Constants.Basic.LANG
import avalon.util.Message
import java.nio.charset.Charset

object MessageChecker {
	@JvmStatic
	fun check(message: Message): Boolean = checkEncode(message)

	@JvmStatic
	private fun checkEncode(message: Message): Boolean {
		val content = message.content
		val charset = Charset.forName("GB2312")
		if (!(content.contentEquals(String(content.toByteArray(charset), charset)))) {
			message.response("${AT(message.senderUid)} ${LANG.getString("group.handler.wrong_encoding")}")
			return false
		}
		return true
	}
}