package avalon.group

import avalon.api.RegisterResponder
import avalon.tool.pool.AvalonPluginPool
import avalon.tool.pool.Constants
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Help : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val apiList = GroupMessageHandler.getApiList()
		val messageShow = StringBuilder()

		@Suppress("LoopToCallChain")
		for (api in apiList.values) {
			if (!GroupMessageHandler.getInstance().isResponderEnable(api))
				continue
			val helpMessage = api.responderInfo().helpMessage
			if (helpMessage.isEmpty())
				continue
			messageShow.append("\n").append(helpMessage)
		}
		for (thisPlugin in AvalonPluginPool.getPluginList()) {
			messageShow.append("以下指令由插件 ").append(thisPlugin.name()).append(" 提供：")
			RegisterResponder.queryAvalonPlugin(thisPlugin).forEach { e -> messageShow.append("\n").append(e.getHelpMessage()) }
		}
		message.response("""This is Avalon. 以下是我的帮助资料：
<关键词>：<触发的作用效果>，所有关键词均忽略大小写并且以avalon开头$messageShow
For Avalon Version v${Constants.Version.AVALON}""")
		// "\n（我才不会告诉你我有一些没有写在这里的彩蛋指令呢~哈哈`(*∩_∩*)′）");
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("avalon (help|帮助)", "显示本内容"),
					Pattern.compile("^avalon (help|帮助)"),
					manageable = false
			)

	override fun instance(): GroupMessageResponder? = this
}