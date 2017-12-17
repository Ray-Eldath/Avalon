package avalon.group

import avalon.api.Flag.AT
import avalon.api.getAllowArray
import avalon.tool.pool.Constants
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
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
		allowList!!.filter { it == senderUid }
				.forEach {
					when (split[2]) {
						"list" -> {
							if (Constants.Basic.DEBUG)
								message.response(map.keys.joinToString())
							if (map.isEmpty())
								message.response("${AT(message)} 管理员：屏蔽列表为空~●▽●")
							else {
								val to = map.keys.joinToString { CURRENT_SERVLET.getGroupSenderNickname(groupUid, it) }
								message.response("${AT(message)} 管理员：屏蔽列表中有：\n$to")
							}
						}
						"add" -> {
							message.response("${AT(message)} 管理员：帐号 $toBan 现已被屏蔽⊙﹏⊙")
							Blacklist.logger.info("Account $toBan is baned by $senderUid : $sender.")
							GroupMessageHandler.addBlackListPeople(toBan, max)
						}
						"remove" -> {
							if (!map.containsKey(toBan))
								message.response("${AT(message)} 管理员：好像帐号 $toBan 没有被屏蔽过呢-。-")
							else {
								message.response("${AT(message)} 管理员：帐号 $toBan 的屏蔽已被解除(^.^)")
								Blacklist.logger.info("Account $toBan is allowed again by $senderUid : $sender.")
								GroupMessageHandler.addBlackListPeople(toBan, 0)
							}
						}
						else -> message.response("${AT(message)} 管理员：您的指示格式不对呢！（｀Δ´）！")
					}
				}
	}

	override fun responderInfo(): ResponderInfo = ResponderInfo(
			Pair("avalon blacklist (list|add|remove)", "查看黑名单、将指定的QQ号添加至黑名单或从黑名单移除"),
			Pattern.compile("^avalon blacklist (list|add|remove)"),
			configIdentifier = arrayOf("Blacklist_basic"),
			manageable = false,
			permission = ResponderPermission.ADMIN)

	override fun instance() = this
}