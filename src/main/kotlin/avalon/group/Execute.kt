package avalon.group

import avalon.api.Flag
import avalon.api.Flag.AT
import avalon.tool.ExecutiveStatus
import avalon.tool.Executives
import avalon.tool.pool.Constants.Basic.MAX_ECHO_LENGTH
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Execute : GroupMessageResponder() {
	private val executive = Executives.EXECUTIVE

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val contentM = message.content
		val split = contentM.split("\n")
		val first = split[0]
		val codeLines = split.subList(1, split.size)
		val lang = first.replace("avalon execute ", "").trim()

		if (!executive.allLanguages().contains(lang)) {
			message.response("${AT(message)} 给定的语言不受支持╮(╯_╰)╭")
			return
		}
		if (codeLines.all { it.isEmpty() }) {
			message.response("${AT(message)} 不允许提交空代码∑(O_O；)")
			return
		}
		val result = executive.execute(lang, codeLines)
		val content =
				when (result.status) {
					ExecutiveStatus.ERROR -> "编译错误或其他致命错误：exitcode: ${result.exitcode} stderr: ${handleOutput(result.stderr)} error: ${handleOutput(result.error)}"
					ExecutiveStatus.STDERR -> "执行错误：exitcode: ${result.exitcode} stderr: ${handleOutput(result.stderr)}"
					ExecutiveStatus.OK -> "执行成功！exitcode: ${result.exitcode} stdout: ${handleOutput(result.stdout)}"
				}
		message.response("${Flag.AT(message)} $content")
	}

	private fun handleOutput(string: String): String {
		val length = string.length
		if (length > MAX_ECHO_LENGTH)
			return string.substring(0, MAX_ECHO_LENGTH) + "...<超长文本截断 原长度：$length>"
		return string.replace("\n", " ")
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("avalon execute <语言>{换行}<代码>", "执行给定代码并回显输出。支持语言列表及代码执行器相关信息请使用`avalon execute info`查看。"),
					Pattern.compile("execute [^info]+")
			)

	override fun instance(): GroupMessageResponder = this
}