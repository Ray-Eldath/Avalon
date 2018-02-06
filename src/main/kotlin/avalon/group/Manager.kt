package avalon.group

import avalon.api.Flag.AT
import avalon.tool.pool.APISurvivePool
import avalon.tool.pool.Constants
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

object Manager : GroupMessageResponder() {

	private val logger = LoggerFactory.getLogger(Manager.javaClass)

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val content = message.content
		val sender = message.senderNickName
		val senderUid = message.senderUid

		if (" " !in content) {
			message.response("${AT(message)} 您的指示格式不对辣！（｀Δ´）！请注意在API触发语句后是否缺少空格~")
			return
		}
		val split = content.toLowerCase().split(" ")
		if (split.size <= 3) {
			message.response("${AT(message)} 您的指示格式不对辣！（｀Δ´）！请注意在API触发语句后是否缺少空格~")
			return
		}
		val action = split[2]
		val apiName = split.subList(3, split.size).joinToString(separator = " ")

		if (Constants.Basic.DEBUG)
			println("$action $apiName")

		var thisAPI = GroupMessageHandler.getGroupResponderByKeywordRegex(apiName)
		if (thisAPI == null) {
			thisAPI = GroupMessageHandler.getGroupResponderByKeywordRegex(apiName + " ")
			if (thisAPI == null) {
				message.response("${AT(message)} 您要操作的指令响应器根本不存在！注意：Manager识别不含指令前缀的指令且暂不支持操作由插件载入的响应器 (╯︵╰,)")
				return
			}
		}

		if (!thisAPI.responderInfo().manageable) {
			message.response("${AT(message)} 您要操作的指令响应器不能被禁止！(´Д` )")
			return
		}

		when (action) {
			"start" -> {
				APISurvivePool.getInstance().setAPISurvive(thisAPI, true)
				message.response("${AT(message)} 您要重启的指令响应器将会重启`(*∩_∩*)′")
				Manager.logger.info("GroupMessageResponder ${thisAPI.javaClass.simpleName} is reopened by $senderUid  : $sender.")
			}
			"stop" -> {
				APISurvivePool.getInstance().setAPISurvive(thisAPI, false)
				message.response("${AT(message)} 您要关闭的指令响应器将会关闭~=-=")
				Manager.logger.info("GroupMessageResponder ${thisAPI.javaClass.simpleName} is closed by $senderUid : $sender.")
			}
			else -> message.response("${AT(message)} 您的指示格式不对辣！（｀Δ´）！")
		}
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("manager (start|stop) <指令响应器触发语句（不含指令前缀）>", "打开或关闭指定的指令响应器"),
					Pattern.compile("manager (start|stop) "),
					manageable = false,
					permission = ResponderPermission.ADMIN
			)

	override fun instance() = this
}