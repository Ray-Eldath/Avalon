package avalon.group

import java.util.regex.Pattern

import avalon.api.GroupConfigUtils
import avalon.tool.Responder.AT
import avalon.util.{GroupConfig, GroupMessage}
import org.slf4j.LoggerFactory

/**
	* Created by Eldath on 2017/1/30 0030.
	*
	* @author Eldath
	*/
object Blacklist extends GroupMessageResponder {
	private val logger = LoggerFactory.getLogger(Blacklist.getClass)

	override def doPost(message: GroupMessage, groupConfig: GroupConfig): Unit = {
		val sender_uid = message.getSenderUid
		val max = GroupMessageHandler.getPunishFrequency
		val content = message.getContent
		val sender = message.getSenderNickName
		val allowList: Array[Long] = GroupConfigUtils.getAllowArray(groupConfig, "Blacklist_basic")
		if (!content.contains(" ")) {
			message.response(AT(message) + " 您的指示格式不对辣！（｀Δ´）！")
			return
		}
		val split = content.split(" ")
		if (split.length < 3) {
			message.response(AT(message) + " 您的指示格式不对辣！（｀Δ´）！")
			return
		}
		val action = split(2)
		val toBan = split(3).toLong
		for (thisAllowUid <- allowList) {
			if (thisAllowUid == sender_uid) if ("add" == action) {
				message.response(AT(message) + " 帐号" + toBan + "现已被屏蔽。")
				Blacklist.logger.info("Account " + toBan + " is baned by " + sender_uid + " : " + sender + ".")
				GroupMessageHandler.getSetBlackListPeopleMap.put(toBan, max)
				return
			}
			else if ("remove" == action) {
				if (!GroupMessageHandler.getSetBlackListPeopleMap.containsKey(toBan)) {
					message.response(AT(message) + " 好像帐号" + toBan + "没有被屏蔽过呢-。-")
					return
				}
				message.response(AT(message) + " 帐号" + toBan + "的屏蔽已被解除(^.^)")
				Blacklist.logger.info("Account " + toBan + " is allowed again by " + sender_uid + " : " + sender + ".")
				GroupMessageHandler.getSetBlackListPeopleMap.put(toBan, 0)
				return
			}
			else {
				message.response(AT(message) + " 您的指示格式不对辣！（｀Δ´）！")
				return
			}
		}
		message.response(AT(message) + " 您没有权限辣！（｀Δ´）！")
	}

	override def permissionIdentifier: Array[String] = Array("Blacklist_basic")

	override def getHelpMessage = "avalon blacklist (add|remove)：<管理员> 将指定的QQ号 添加至黑名单或从黑名单移除"

	override def getKeyWordRegex: Pattern = Pattern.compile("avalon blacklist add |avalon blacklist remove ")

	override def instance: GroupMessageResponder = this
}