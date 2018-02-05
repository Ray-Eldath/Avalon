package avalon.group

import avalon.api.RegisterResponder
import avalon.tool.pool.AvalonPluginPool
import avalon.tool.pool.Constants
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Help : GroupMessageResponder() {
	private val sent: String by lazy {
		val apiTriple = GroupMessageHandler.apiTriples
		val messageShow = StringBuilder()

		var prefixString = Constants.Basic.DEFAULT_PREFIX.joinToString(separator = "|")
		if (Constants.Basic.DEFAULT_PREFIX.size > 1)
			prefixString = "($prefixString)"

		@Suppress("LoopToCallChain")
		for (api in apiTriple.map { it.instance }.sortedBy { it.responderInfo().helpMessage.first }) {
			val flags = ArrayList<String>()
			if (!GroupMessageHandler.isResponderEnable(api))
				flags.add("<已禁用>")
			val info = api.responderInfo()
			val helpMessage = info.helpMessage
			if (helpMessage.first.isEmpty() || helpMessage.second.isEmpty())
				continue
			if (info.permission == ResponderPermission.ADMIN)
				flags.add("<管理员>")
			else if (info.permission == ResponderPermission.OWNER)
				flags.add("<所有者>")
			messageShow.append("\n$prefixString")
					.append(helpMessage.first)
					.append("：")
					.append(if (flags.isEmpty()) "" else flags.joinToString(separator = " ", postfix = " "))
					.append(helpMessage.second)
		}
		for (thisPlugin in AvalonPluginPool.getPluginList()) {
			messageShow.append("以下指令由插件 ").append(thisPlugin.name()).append(" 提供：")
			RegisterResponder.queryAvalonPlugin(thisPlugin).forEach { e -> messageShow.append("\n").append(e.getHelpMessage()) }
		}
		val displayPrefix = Constants.Basic.DEFAULT_PREFIX.joinToString(separator = "或").replace(" ", "")
		"""This is Avalon. 以下是我的帮助资料：
<关键词>：<触发的作用效果>，所有关键词均忽略大小写并且以给定前缀`$displayPrefix`开头$messageShow
For Avalon Version v${Constants.Version.AVALON}"""
		// "\n（我才不会告诉你我有一些没有写在这里的彩蛋指令呢~哈哈`(*∩_∩*)′）");
	}

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		message.response(sent)
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("(help|帮助)", "显示本内容"),
					Pattern.compile("help|帮助"),
					manageable = false
			)

	override fun instance(): GroupMessageResponder? = this
}