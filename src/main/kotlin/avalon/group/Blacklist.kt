package avalon.group

import avalon.api.Flag.at
import avalon.api.getAllowArray
import avalon.tool.pool.Constants
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.tool.pool.Constants.Basic.LANG
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

/**
 * Created by Eldath on 2017/10/5 0030.
 *
 * @author Eldath
 */
object Blacklist : GroupMessageResponder() {
	private val logger = LoggerFactory.getLogger(Blacklist.javaClass)

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val senderUid = message.senderUid
		val max = GroupMessageHandler.punishFrequency
		val content = message.content
		val sender = message.senderNickName
		val groupUid = message.groupUid
		val allowList = getAllowArray(groupConfig, "Blacklist_basic")
		val split = content.split(" ")
		val toBan = if (content.contains(Regex("add|remove"))) split[3].toLong() else 0
		val map = GroupMessageHandler.blacklistPeopleMap
		allowList!!.first { it == senderUid }.apply {
			when (split[2]) {
				"list" -> {
					if (Constants.Basic.DEBUG)
						message.response(map.keys.joinToString())
					if (map.isEmpty())
						message.response("${at(message)} ${LANG.getString("group.blacklist.empty_banned_list")}")
					else {
						val to = map.keys.joinToString { CURRENT_SERVLET.getGroupSenderNickname(groupUid, it) }
						message.response("${at(message)} ${LANG.getString("group.blacklist.banned_list_has").format(to)}")
					}
				}
				"add" -> {
					message.response("${at(message)} ${String.format(LANG.getString("group.blacklist.account_now_banned"), toBan)}")
					Blacklist.logger.info("account $toBan is baned by $senderUid : $sender.")
					GroupMessageHandler.addBlackListPeople(toBan, max)
				}
				"remove" -> {
					if (!map.containsKey(toBan))
						message.response("${at(message)} ${String.format(LANG.getString("group.blacklist.account_not_banned"), toBan)}")
					else {
						message.response("${at(message)} ${String.format(LANG.getString("group.blacklist.account_now_released"), toBan)}")
						Blacklist.logger.info("account $toBan is allowed again by $senderUid : $sender.")
						GroupMessageHandler.addBlackListPeople(toBan, 0)
					}
				}
				else -> message.response("${at(message)} ${LANG.getString("group.blacklist.incorrect")}")
			}
		}
	}

	override fun responderInfo(): ResponderInfo = ResponderInfo(
			Pair("blacklist (list|add|remove)", LANG.getString("group.blacklist.help")),
			Pattern.compile("blacklist (list|add|remove)"),
			configIdentifier = arrayOf("Blacklist_basic"),
			manageable = false,
			permission = ResponderPermission.ADMIN)

	override fun instance() = this
}