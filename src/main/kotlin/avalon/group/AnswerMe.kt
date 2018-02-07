package avalon.group

import avalon.api.Flag.AT
import avalon.tool.XiaoIceResponder
import avalon.tool.pool.Constants.Basic.LANG
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.apache.commons.lang3.StringUtils
import java.util.regex.Pattern

/**
 * Created by Eldath on 2017/10/5 0029.
 *
 * @author Eldath
 */
object AnswerMe : GroupMessageResponder() {

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		var content = message.content.trim().toLowerCase().replace(Regex("[\\pP\\p{Punct}]"), "")
		var text = content
		val regex = responderInfo().keyWordRegex
		text = text.replace(regex.toRegex(), "")
		if ("" == text.replace(" ", "")) {
			message.response("${AT(message)} ${LANG.getString("group.answer_me.empty_content")}")
			return
		}
		if (StringUtils.isAlpha(text)) if (text.length < 5) {
			message.response("${AT(message)} ${LANG.getString("group.answer_me.short_content")}")
			return
		} else if (text.length < 3) {
			message.response(AT(message) + " ")
			return
		}
		content = content.replace(regex.toRegex(), "")
		val responseXiaoIce = XiaoIceResponder.responseXiaoIce(content) ?: return
		message.response(AT(message) + " " + responseXiaoIce)
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("answer me", LANG.getString("group.answer_me.help")),
					Pattern.compile("answer me")
			)

	override fun instance() = this
}