package avalon.group

import avalon.api.Flag.AT
import avalon.api.getAllowArray
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
		val max = GroupMessageHandler.getPunishFrequency()
		val content = message.content
		val sender = message.senderNickName
		val allowList = getAllowArray(groupConfig, "Blacklist_basic")
		val split = content.split(" ")
		val toBan = split[3].toLong()
		allowList!!
				.filter { it == senderUid }
				.forEach {
					when (split[2]) {
						"add" -> {
							message.response("${AT(message)} 帐号 $toBan 现已被屏蔽。")
							Blacklist.logger.info("Account $toBan is baned by $senderUid : $sender.")
							GroupMessageHandler.getSetBlackListPeopleMap().put(toBan, max)
							return
						}
						"remove" -> {
							if (!GroupMessageHandler.getSetBlackListPeopleMap().containsKey(toBan)) {
								message.response("${AT(message)} 管理员：好像帐号 $toBan 没有被屏蔽过呢-。-")
								return
							}
							message.response("${AT(message)} 管理员：帐号 $toBan 的屏蔽已被解除(^.^)")
							Blacklist.logger.info("Account $toBan is allowed again by $senderUid : $sender.")
							GroupMessageHandler.getSetBlackListPeopleMap().put(toBan, 0)
							return
						}
						else -> {
							message.response("${AT(message)} 管理员：您的指示格式不对呢！（｀Δ´）！")
							return
						}
					}
				}
		message.response(AT(message) + " 您没有权限辣！（｀Δ´）！")
	}

	override fun responderInfo(): ResponderInfo = ResponderInfo(
			Pair("avalon blacklist (add|remove)", "将指定的QQ号 添加至黑名单或从黑名单移除"),
			Pattern.compile("^avalon blacklist (add|remove)"),
			configIdentifier = arrayOf("Blacklist_basic"),
			manageable = false,
			permission = ResponderPermission.ADMIN)

	override fun instance() = this
}