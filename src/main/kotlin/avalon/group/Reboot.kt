package avalon.group

import avalon.tool.pool.Constants.Basic.CURRENT_PATH
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Reboot : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		message.response("正在重启...请等待至少10s后使用`avalon version`查看重启是否成功")

		CURRENT_SERVLET.reboot()

		val os = System.getProperty("os.name").toLowerCase()
		val cmd: String? =
				when {
					os.contains("linux") -> "$CURRENT_PATH/Avalon"
					os.contains("windows") -> "cmd /c start $CURRENT_PATH\\Avalon.bat"
					else -> null
				}

		if (cmd == null) {
			message.response("操作失败：不支持的操作系统（话说你咋启动Avalon的？")
			return
		}

		val process = Runtime.getRuntime().exec(cmd)
		process.inputStream.close()
		process.errorStream.close()
		process.inputStream.close()

		Thread.sleep(5000)

		Runtime.getRuntime().halt(0)
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
                    Pair("reboot", "重启Avalon和后端程序。将造成服务中断。"),
                    Pattern.compile("reboot"),
					permission = ResponderPermission.OWNER,
					manageable = false
			)

	override fun instance(): GroupMessageResponder? = this
}