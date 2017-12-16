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
			Constants.Basic.CURRENT_SERVLET.shutdown()
		} catch (ignore: UnsupportedOperationException) {
			System.exit(0)
		}
		System.exit(0)
	}


	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("avalon shutdown", "<所有者> 退出Avalon。"),
					Pattern.compile("^avalon (shutdown|exit)"),
					permission = ResponderPermission.OWNER,
					manageable = false
			)

	override fun instance() = this
}