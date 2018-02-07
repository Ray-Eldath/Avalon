package avalon.group

import avalon.tool.pool.Constants.Basic.LANG
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

object Shutdown : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		LoggerFactory.getLogger(Shutdown.javaClass).warn("Avalon is stopped remotely by ${message.senderUid}:${message.senderNickName} on ${message.groupUid}:${message.groupName} at ${message.time.toString().replace("T", " ")}")
		System.exit(0) // 此处会执行hook 而hook会关闭SERVLET.
	}


	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("(shutdown|exit)", LANG.getString("group.shutdown.help")),
					Pattern.compile("(shutdown|exit)"),
					permission = ResponderPermission.OWNER,
					manageable = false
			)

	override fun instance() = this
}