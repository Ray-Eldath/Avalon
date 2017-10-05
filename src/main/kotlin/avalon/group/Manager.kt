package avalon.group

import avalon.api.Flag.AT
import avalon.api.getAllowArray
import avalon.tool.pool.APISurvivePool
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

object Manager : GroupMessageResponder() {

	private val logger = LoggerFactory.getLogger(Manager.javaClass)
	private val canNotBanAPI = arrayOf(Shutdown.instance(), Flush.instance(), Manager.instance())

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val restartAllowUid = getAllowArray(groupConfig, "Manager_restart")!!
		val allowPeople = getAllowArray(groupConfig, "Manager_basic")!!
		val stopAllowUid = getAllowArray(groupConfig, "Manager_stop")!!

		val content = message.content
		val sender = message.senderNickName
		val senderUid = message.senderUid
		for (thisFollowFriend in allowPeople) {
			if (senderUid == thisFollowFriend) {
				if (" " !in content) {
					message.response("@$sender 您的指示格式不对辣！（｀Δ´）！请注意在API触发语句后是否缺少空格~")
					return
				}
				val apiName = content.toLowerCase()
						.replace("avalon manager stop ", "")
						.replace("avalon manager start ", "")
				val thisAPI = GroupMessageHandler.getInstance().getGroupResponderByKeyword(apiName)
				val action = content.toLowerCase()
						.replace("avalon manager ", "")
						.replace(apiName, "").trim()
				if (thisAPI == null) {
					message.response(AT(message) + " 您要操作的指令响应器根本不存在！注意：Manager暂不支持操作由插件载入的响应器 (╯︵╰,)")
					return
				}
				for (thisCanNotBanRunner in canNotBanAPI) {
					if (thisAPI == thisCanNotBanRunner) {
						message.response(AT(message) + " 您要操作的指令响应器可不能被禁止啊！(´Д` )")
						return
					}
				}
				if ("start" == action)
					for (thisAllowStartUid in restartAllowUid) {
						if (thisAllowStartUid == senderUid) {
							APISurvivePool.getInstance().setAPISurvive(thisAPI, true)
							message.response(AT(message) + " 您要重启的指令响应器将会重启`(*∩_∩*)′")
							Manager.logger.info("BaseGroupMessageResponder " + thisAPI.javaClass.name +
									" is reopened by " + senderUid + " : " + sender + ".")
							return
						}
					}
				if ("stop" == action)
					for (thisStopAllowUid in stopAllowUid) {
						if (thisStopAllowUid == senderUid) {
							APISurvivePool.getInstance().setAPISurvive(thisAPI, false)
							message.response(AT(message) + " 您要关闭的指令响应器将会关闭~=-=")
							Manager.logger.info("BaseGroupMessageResponder " + thisAPI.javaClass.name +
									" is closed by " + senderUid + " : " + sender + ".")
							return
						}
					}
				else {
					message.response(AT(message) + " 您的指示格式不对辣！（｀Δ´）！")
					return
				}
			}
		}
		message.response(AT(message) + " 您没有权限啦！(゜д゜)")
	}

	override fun permissionIdentifier() = arrayOf("Manager_restart", "Manager_stop", "Manager_basic")

	override fun getHelpMessage() = "avalon manager (start|stop) <指令响应器触发语句>：<管理员> 打开或关闭控制指令响应器"

	override fun getKeyWordRegex(): Pattern = Pattern.compile("^avalon manager (start|stop)")

	override fun instance() = this
}