package avalon.group

import avalon.api.RegisterResponder
import avalon.tool.pool.AvalonPluginPool
import avalon.tool.pool.Constants
import avalon.tool.pool.Constants.Basic.LANG
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

		val disabled = LANG.getString("group.help.disabled")
		val admin = LANG.getString("group.help.admin")
		val owner = LANG.getString("group.help.owner")

		@Suppress("LoopToCallChain")
		for (api in apiTriple.map { it.instance }.sortedBy { it.responderInfo().helpMessage.first }) {
			val flags = ArrayList<String>()
			if (!GroupMessageHandler.isResponderEnable(api))
				flags.add(disabled)
			val info = api.responderInfo()
			val helpMessage = info.helpMessage
			if (helpMessage.first.isEmpty() || helpMessage.second.isEmpty())
				continue
			if (info.permission == ResponderPermission.ADMIN)
				flags.add(admin)
			else if (info.permission == ResponderPermission.OWNER)
				flags.add(owner)
			messageShow.append("\n$prefixString")
					.append(helpMessage.first)
					.append("：")
					.append(if (flags.isEmpty()) "" else flags.joinToString(separator = " ", postfix = " "))
					.append(helpMessage.second)
		}
		for (thisPlugin in AvalonPluginPool.getPluginList()) {
			val temp = RegisterResponder.queryAvalonPlugin(thisPlugin)
					.forEach { e -> messageShow.append("\n").append(e.getHelpMessage()) }
			messageShow.append(LANG.getString("group.help.plugin_command").format(thisPlugin.name(), temp))
		}
		val displayPrefix = Constants.Basic.DEFAULT_PREFIX
				.joinToString(separator = LANG.getString("base.or"))
				.replace(" ", "")
		"""${LANG.getString("group.help.reply").format(displayPrefix)}$messageShow
For Avalon Version v${Constants.Version.AVALON}"""
		// "\n（我才不会告诉你我有一些没有写在这里的彩蛋指令呢~哈哈`(*∩_∩*)′）");
	}

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		message.response(sent)
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("help", LANG.getString("group.help.help")),
					Pattern.compile("help"),
					manageable = false
			)

	override fun instance(): GroupMessageResponder? = this
}