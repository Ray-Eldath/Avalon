package avalon.group

import avalon.api.Flag
import avalon.api.Flag.at
import avalon.tool.ExecutiveStatus
import avalon.tool.Executives
import avalon.tool.pool.Constants.Basic.LANG
import avalon.tool.pool.Constants.Basic.MAX_ECHO_LENGTH
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Execute : GroupMessageResponder() {
	private val executive = Executives.EXECUTIVE

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val contentM = message.content
		val single = !contentM.contains("\n")

		val codes: List<String>
		val lang: String
		if (single) {
			val split = contentM.split(" ") // avalon execute python print(2+3)
			val size = split.size
			if (size < 4) {
				message.response("${at(message)} ${LANG.getString("group.execute.incorrect")}")
				return
			}
			codes = split.subList(3, size)
			lang = split[2]
		} else {
			val split = contentM.split("\n")
			val first = split[0]
			codes = split.subList(1, split.size)
			lang = first.replace("avalon execute ", "")
				.replace("avalon ex ", "").trim()
		}

		if (!executive.allLanguages().contains(lang)) {
			message.response("${at(message)} ${LANG.getString("group.execute.unsupported_language")}")
			return
		}
		if (codes.all { it.isEmpty() }) {
			message.response("${at(message)} ${LANG.getString("group.execute.empty_code")}")
			return
		}
		val result = executive.execute(lang, codes)
		val exitcode = result.exitcode
		val stderr = handleOutput(result.stderr)
		val content =
			when (result.status) {
				ExecutiveStatus.ERROR -> LANG.getString("group.execute.error")
					.format("exitcode: $exitcode} stderr: $stderr error: ${handleOutput(result.error)}")
				ExecutiveStatus.STDERR -> LANG.getString("group.execute.stderr")
					.format("exitcode: $exitcode stderr: $stderr")
				ExecutiveStatus.OK -> LANG.getString("group.execute.ok")
					.format("exitcode: $exitcode stdout: ${handleOutput(result.stdout)}")
			}
		message.response("${Flag.at(message)} $content")
	}

	private fun handleOutput(string: String): String {
		val length = string.length
		if (length > MAX_ECHO_LENGTH)
			return string.substring(0, MAX_ECHO_LENGTH) + "...${String.format(LANG.getString("group.execute.truncated"), length)}"
		return string.replace("\n", " ")
	}

	override fun responderInfo(): ResponderInfo =
		ResponderInfo(
			Pair("(ex|execute) ${LANG.getString("group.execute.help.first")}",
				LANG.getString("group.execute.help.second").format("avalon execute info")),
			Pattern.compile("(ex|execute) [^info]*")
		)

	override fun instance(): GroupMessageResponder = this
}