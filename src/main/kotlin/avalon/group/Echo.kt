package avalon.group

import avalon.tool.pool.Constants
import avalon.tool.pool.Constants.Basic.LANG
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
		val content = message.content.replace(
				Regex(Constants.Basic.DEFAULT_PREFIX.joinToString(separator = "|") +
						responderInfo().keyWordRegex),
				"")
		message.response(content)
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("(echo|repeat)", LANG.getString("group.echo.help")),
					Pattern.compile("(echo|repeat) ")
			)

	override fun instance() = this
}