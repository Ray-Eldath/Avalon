package avalon.group

import avalon.tool.pool.Constants
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

object Shutdown : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		LoggerFactory.getLogger(Shutdown.javaClass).warn("Avalon is stopped remotely by ${message.senderUid}:${message.senderNickName} on ${message.groupUid}:${message.groupName} at ${message.time.toString().replace("T", " ")}")
		try {
			Constants.Basic.currentServlet.shutdown()
		} catch (ignore: UnsupportedOperationException) {
			System.exit(0)
		}
		System.exit(0)
	}

	override fun permission(): ResponderPermission = ResponderPermission.OWNER

	override fun getHelpMessage() = "avalon shutdown：<所有者> 退出Avalon。"

	override fun getKeyWordRegex(): Pattern = Pattern.compile("^avalon (shutdown|exit)")

	override fun instance() = this
}