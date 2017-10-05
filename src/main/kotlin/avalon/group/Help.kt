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

		for (api in apiList.values) {
			if (!GroupMessageHandler.getInstance().isResponderEnable(api))
				continue
			val helpMessage = api.getHelpMessage()
			if (helpMessage.isEmpty())
				continue
			messageShow.append("\n").append(api.getHelpMessage())
		}
		for (thisPlugin in AvalonPluginPool.getPluginList()) {
			messageShow.append("以下指令由插件 ").append(thisPlugin.name()).append(" 提供：")
			RegisterResponder.queryAvalonPlugin(thisPlugin).forEach { e -> messageShow.append("\n").append(e.getHelpMessage()) }
		}
		message.response("This is Avalon. 以下是我的帮助资料：\n" +
				"<关键词>：<触发的作用效果>，所有关键词均忽略大小写并且以avalon开头" + messageShow + "\nFor Avalon Version v" +
				Constants.Version.avalon)
		// "\n（我才不会告诉你我有一些没有写在这里的彩蛋指令呢~哈哈`(*∩_∩*)′）");
	}

	override fun getHelpMessage(): String = "avalon (help|帮助)：显示本内容"

	override fun getKeyWordRegex(): Pattern = Pattern.compile("^avalon (help|帮助)")

	override fun instance(): GroupMessageResponder? = this
}