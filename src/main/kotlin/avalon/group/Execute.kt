package avalon.group

import avalon.api.Flag
import avalon.tool.ExecutiveStatus
import avalon.tool.Executives
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Execute : GroupMessageResponder {
	private val executive = Executives.EXECUTIVE

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val contentM = message.content
		val split = contentM.split("\n")
		val first = split[0]
		val codeLines = split.subList(1, split.size)
		val lang = first.replace("avalon execute ", "").trim()

		if (!executive.allLanguages().contains(lang)) {
			message.response("给定的语言不受支持╮(╯_╰)╭")
			return
		}
		if (codeLines.all { it.isEmpty() }) {
			message.response("不允许提交空代码∑(O_O；)")
			return
		}
		val result = executive.execute(lang, codeLines)
		val content =
				when (result.status) {
					ExecutiveStatus.ERROR -> "编译错误或其他致命错误：exitcode: ${result.exitcode} error: \n${result.error}"
					ExecutiveStatus.STDERR -> "执行错误：exitcode: ${result.exitcode} stderr: \n${result.stderr}"
					ExecutiveStatus.OK -> "执行成功！exitcode: ${result.exitcode} stdout: ${result.stdout}"
				}
		message.response("${Flag.AT(message)} $content")
	}

	override fun getHelpMessage(): String = "avalon execute <语言>{换行}<代码>：执行给定代码并回显输出。支持语言列表请使用`avalon execute languages`查看。"

	override fun getKeyWordRegex(): Pattern = Pattern.compile("avalon execute [^languages]+")

	override fun instance(): GroupMessageResponder = this
}