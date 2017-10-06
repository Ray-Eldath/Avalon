package avalon.group

import avalon.tool.pool.Constants
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

object Shutdown : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val admins = groupConfig.admin
		for (admin in admins) {
			if (admin == message.senderUid) {
				try {
					Constants.Basic.currentServlet.shutdown()
				} catch (e: UnsupportedOperationException) {
					e.printStackTrace()
					System.exit(0)
				}
				LoggerFactory.getLogger(Shutdown.javaClass).warn("Avalon is stopped remotely by ${message.senderUid}:${message.senderNickName} on ${message.groupUid}:${message.groupName} at ${message.time.toString().replace("T", " ")}")
				System.exit(0)
			}
		}
	}

	override fun getHelpMessage() = "avalon (shutdown|exit)：<管理员> 退出Avalon。"

	override fun getKeyWordRegex(): Pattern = Pattern.compile("^avalon (shutdown|exit)")

	override fun instance() = this
}