package avalon.group

import avalon.api.Flag.at
import avalon.tool.pool.APISurvivePool
import avalon.tool.pool.Constants
import avalon.tool.pool.Constants.Basic.LANG
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

		val incorrect = "${at(message)} ${LANG.getString("group.manager.incorrect_e")}"
		if (" " !in content) {
			message.response(incorrect)
			return
		}
		val split = content.toLowerCase().split(" ")
		if (split.size <= 3) {
			message.response(incorrect)
			return
		}
		val action = split[2]
		val apiName = split.subList(3, split.size).joinToString(separator = " ")

		if (Constants.Basic.DEBUG)
			println("$action $apiName")

		var thisAPI = GroupMessageHandler.getGroupResponderByKeywordRegex(apiName)
		if (thisAPI == null) {
			thisAPI = GroupMessageHandler.getGroupResponderByKeywordRegex("$apiName ")
			if (thisAPI == null) {
				message.response("${at(message)} ${LANG.getString("group.manager.not_exist")}")
				return
			}
		}

		if (!thisAPI.responderInfo().manageable) {
			message.response("${at(message)} ${LANG.getString("group.manager.can_not_stop")}")
			return
		}

		when (action) {
			"start" -> {
				APISurvivePool.getInstance().setAPISurvive(thisAPI, true)
				message.response("${at(message)} ${LANG.getString("group.manager.start")}")
				Manager.logger.info("GroupMessageResponder ${thisAPI.javaClass.simpleName} is reopened by $senderUid  : $sender.")
			}
			"stop" -> {
				APISurvivePool.getInstance().setAPISurvive(thisAPI, false)
				message.response("${at(message)} ${LANG.getString("group.manager.stop")}")
				Manager.logger.info("GroupMessageResponder ${thisAPI.javaClass.simpleName} is closed by $senderUid : $sender.")
			}
			else -> message.response("${at(message)} ${LANG.getString("group.manager.incorrect")}")
		}
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("manager (start|stop) ${LANG.getString("group.manager.help.first")}", LANG.getString("group.manager.help.second")),
					Pattern.compile("manager (start|stop) "),
					manageable = false,
					permission = ResponderPermission.ADMIN
			)

	override fun instance() = this
}