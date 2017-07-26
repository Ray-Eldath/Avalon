package avalon.group

import java.util.regex.Pattern

import avalon.tool.Responder.AT
import avalon.tool.pool.APISurvivePool
import avalon.tool.system.ConfigSystem
import avalon.util.GroupMessage
import org.slf4j.LoggerFactory

/**
	* Created by Eldath on 2017/1/29 0029.
	*
	* @author Eldath
	*/
object Manager extends GroupMessageResponder {
	private val logger = LoggerFactory.getLogger(Manager.getClass)
	private val restartAllowUid = ConfigSystem.getInstance.getCommandAllowArray("Manager_restart")
	private val allowPeople = ConfigSystem.getInstance.getCommandAllowArray("Manager_basic")
	private val stopAllowUid = ConfigSystem.getInstance.getCommandAllowArray("Manager_stop")
	private val canNotBanAPI = Array[GroupMessageResponder](Shutdown.instance, Flush.instance, Manager.instance)

	override def doPost(message: GroupMessage): Unit = {
		val content = message.getContent
		val sender = message.getSenderNickName
		val senderUid = message.getSenderUid
		for (thisFollowFriend <- Manager.allowPeople) {
			if (senderUid == thisFollowFriend) {
				if (!content.contains(" ")) {
					message.response("@" + sender + " 您的指示格式不对辣！（｀Δ´）！请注意在API触发语句后是否缺少空格~")
					return
				}
				val apiName = content.toLowerCase.replace("avalon manager stop ", "").replace("avalon manager start ", "")
				val thisAPI = GroupMessageHandler.getInstance.getGroupResponderByKeyword(apiName)
				val action = content.toLowerCase.replace("avalon manager ", "").replace(apiName, "").trim
				if (thisAPI == null) {
					message.response(AT(message) + " 您要操作的指令响应器根本不存在！注意：Manager暂不支持操作由插件载入的响应器 (╯︵╰,)")
					return
				}
				for (thisCanNotBanRunner <- Manager.canNotBanAPI) {
					if (thisAPI == thisCanNotBanRunner) {
						message.response(AT(message) + " 您要操作的指令响应器可不能被禁止啊！(´Д` )")
						return
					}
				}
				if ("start" == action) for (thisAllowStartUid <- Manager.restartAllowUid) {
					if (thisAllowStartUid == senderUid) {
						APISurvivePool.getInstance.setAPISurvive(thisAPI, true)
						message.response(AT(message) + " 您要重启的指令响应器将会重启`(*∩_∩*)′")
						Manager.logger.info("BaseGroupMessageResponder " + thisAPI.getClass.getName +
							" is reopened by " + senderUid + " : " + sender + ".")
						return
					}
				}
				if ("stop" == action) for (thisStopAllowUid <- Manager.stopAllowUid) {
					if (thisStopAllowUid == senderUid) {
						APISurvivePool.getInstance.setAPISurvive(thisAPI, false)
						message.response(AT(message) + " 您要关闭的指令响应器将会关闭~=-=")
						Manager.logger.info("BaseGroupMessageResponder " + thisAPI.getClass.getName +
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

	override def getHelpMessage = "avalon manager (start|stop)：<管理员> 打开或关闭控制指令响应器"

	override def getKeyWordRegex: Pattern = Pattern.compile("avalon manager ")

	override def instance: GroupMessageResponder = this
}