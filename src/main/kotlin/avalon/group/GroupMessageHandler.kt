package avalon.group

import avalon.api.CustomGroupResponder
import avalon.api.Flag.AT
import avalon.api.RegisterResponder.register
import avalon.function.Recorder
import avalon.main.MainServer
import avalon.main.MessageChecker
import avalon.tool.APIRateLimit
import avalon.tool.ObjectCaster
import avalon.tool.ObjectCaster.toStringArray
import avalon.tool.pool.APISurvivePool
import avalon.tool.pool.AvalonPluginPool
import avalon.tool.pool.Constants
import avalon.tool.pool.Constants.Basic.DEBUG_MESSAGE_GROUP_UID
import avalon.tool.pool.Constants.Basic.DEBUG_MESSAGE_UID
import avalon.tool.pool.Variables.Cooling_Not_Notice_Times
import avalon.tool.pool.Variables.Cooling_Noticed
import avalon.tool.system.Configs
import avalon.tool.system.GroupConfigs
import avalon.tool.system.RunningData
import avalon.util.ConfigurationError
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.apache.commons.lang3.ArrayUtils
import org.slf4j.LoggerFactory
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Eldath Ray on 2017/3/30.
 *
 * @author Eldath Ray
 */
object GroupMessageHandler {

	internal fun getGroupResponderByKeywordRegex(keyword: String): GroupMessageResponder? {
		for ((key, value) in apiList)
			if (key.matcher(keyword).find())
				return value
		return null
	}

	internal fun isResponderEnable(api: GroupMessageResponder): Boolean = if (!enableMap.containsKey(api)) true else enableMap[api]!!

	fun handle(message: GroupMessage) {
		val groupUid = message.groupUid
		val sender = message.senderNickName
		val senderUid = message.senderUid

		val groupConfig =
				if (groupUid != DEBUG_MESSAGE_GROUP_UID)
					GroupConfigs.instance().getConfig(groupUid)
				else
					GroupConfig(true, false, DEBUG_MESSAGE_UID, longArrayOf(), longArrayOf(), listOf())

		if (groupConfig == null) {
			LOGGER.warn("listened message from not configured group " +
					groupUid + " . Ignored this message. Please config this group in `.\\group.json`.")
			return
		}
		if (groupConfig.isRecord && !Constants.Basic.DEBUG)
			Recorder.getInstance().recodeGroupMessage(message)
		if (!groupConfig.isListen)
			return
		if (ArrayUtils.contains(groupConfig.blacklist, senderUid))
			return

		val adminUidStream = Arrays.stream(groupConfig.admin)
		val admin = adminUidStream.anyMatch { e -> e == senderUid }

		if (Constants.Setting.Block_Words_Punishment_Mode_Enabled)
			if (blacklistPeopleMap.containsKey(senderUid)) {
				if (blacklistPeopleMap[senderUid]!! >= punishFrequency) {
					LOGGER.info("Account $senderUid : $sender was blocked. Please entered `avalon blacklist remove $senderUid` to group $groupUid : ${message.groupName} if you really want to unblock this account.")
					if (!admin)
						message.response(AT(message) + " 您的帐号由于发送过多不允许关键字，现已被屏蔽~o(╯□╰)o！")
					return
				}
			}

		for ((key, value) in apiList) {
			val info = value.responderInfo()

			if (patternCheck(key, message, admin)) {
				if (!APISurvivePool.getInstance().isSurvive(value)) {
					if (!APISurvivePool.getInstance().isNoticed(value)) {
						if (!info.keyWordRegex.matcher("+1s").find())
							message.response("${AT(message)} 对不起，您调用的指令响应器目前已被停止；注意：此消息仅会显示一次。")
						APISurvivePool.getInstance().setNoticed(value)
					}
				} else if (MessageChecker.check(message) && isResponderEnable(value) &&
						permissionCheck(info.permission, groupConfig, message))
					value.doPost(message, groupConfig)
				return
			}
		}

		for ((key, value) in customApiList) {
			if (patternCheck(key, message, admin)) {
				if (MessageChecker.check(message))
					value.doPost(message, groupConfig)
				return
			}
		}
	}

	private fun permissionCheck(permission: ResponderPermission, config: GroupConfig, message: GroupMessage): Boolean {
		val senderUid = message.senderUid
		var result = false

		if (senderUid == DEBUG_MESSAGE_UID)
			return true

		when {
			permission === ResponderPermission.ADMIN -> result = ArrayUtils.contains(config.admin, senderUid)
			permission === ResponderPermission.OWNER -> result = config.owner == senderUid
			permission === ResponderPermission.ALL -> result = true
		}

		if (!result)
			message.response("${AT(message)} 致命错误：需要`sudo`以执行此操作！（雾")
		return result
	}

	private fun patternCheck(key: Pattern, groupMessage: GroupMessage, admin: Boolean): Boolean {
		val lowerContent = groupMessage.content.toLowerCase()
		val time = groupMessage.timeLong
		if (!key.matcher(lowerContent).find())
			return false

		// 屏蔽词判断
		for (thisBlockString in blockWordList)
			if (groupMessage.content.trim { it <= ' ' }.toLowerCase().replace("[\\pP\\p{Punct}]".toRegex(), "").contains(thisBlockString)) {
				var notice = "您发送的消息含有不允许的关键词！"
				if (Constants.Setting.Block_Words_Punishment_Mode_Enabled && !admin) {
					notice = "您发送的消息含有不允许的关键词，注意：${punishFrequency}次发送不允许关键词后帐号将被屏蔽！⊙﹏⊙!"
					blackListPlus(groupMessage.senderUid)
				}
				groupMessage.response(AT(groupMessage) + " " + notice)
				return false
			}

		// 冷却判断
		if (!cooling.trySet(time)) {
			Cooling_Not_Notice_Times += 1
			if (Cooling_Not_Notice_Times > 4 || !Cooling_Noticed) {
				if (key.matcher("+1s").find())
					return false
				groupMessage.response("${AT(groupMessage)} 对不起，您的指令超频。${coolingDuration}ms内仅能有一次指令输入，未到${coolingDuration}ms内的输入将被忽略。")
				Cooling_Noticed = true
			}
			if (Cooling_Not_Notice_Times >= 4) {
				Cooling_Noticed = false
				Cooling_Not_Notice_Times = 0
			}
			LOGGER.info("cooling blocked message ${groupMessage.id} sent by ${groupMessage.senderUid} in ${groupMessage.groupName}.")
			return false
		}
		return true
	}

	private fun blackListPlus(senderUid: Long) {
		val second = blacklistPeopleMap[senderUid]
		blacklistPeopleMap.put(senderUid, if (second == null) 1 else second + 1)
	}

	val apiList = LinkedHashMap<Pattern, GroupMessageResponder>()
	private val apiNameMap = HashMap<String, GroupMessageResponder>()
	private val customApiList = LinkedHashMap<Pattern, CustomGroupResponder>()
	private val enableMap = HashMap<GroupMessageResponder, Boolean>()
	internal val blacklistPeopleMap = HashMap<Long, Int>()

	private val disableNotAllowedResponder = ArrayList<GroupMessageResponder>()

	val blockWordList: Array<String> = toStringArray(Configs.Companion.instance().getConfigArray("block_words"))
	val punishFrequency = Configs.Companion.instance().get("block_words_punish_frequency") as Int
	private val coolingDuration = ObjectCaster.toLong(Configs.get("cooling_duration"))

	private val cooling = APIRateLimit(coolingDuration)
	private val LOGGER = LoggerFactory.getLogger(GroupMessageHandler::class.java)

	init {
		/*
	     * 指令优先级排序依据：单词 >> 多词，管理类 >> 服务类 >> 娱乐类，触发类 >> 自由类
	    */
		// 管理类
		register(Shutdown)
		register(Reboot)
		register(Flush)
		register(Manager)
		register(Blacklist)
		register(Quote)
		// 服务类
		register(Help, true)
		register(Version, true)
		register(ShowAdmin)
		register(Echo)
		register(ExecuteInfo)
		register(Execute)
		// 娱乐类
		register(Wolfram)
		register(Hitokoto)
		register(Mo)
		register(AnswerMe)
	}

	init {
		val `object` = Configs.getJSONObject("responders")
		val enable = toStringArray(`object`.getJSONArray("enable").toList().toTypedArray())
		val disable = toStringArray(`object`.getJSONArray("disable").toList().toTypedArray())


		for (thisDisable in disable)
			enableMap.put(apiNameMap[thisDisable]!!, false)

		enable.map { apiNameMap[it] }
				.filterNot { enableMap.containsKey(it) }
				.forEach { enableMap.put(it!!, true) }

		apiList.values.filterNot { enableMap.containsKey(it) }
				.forEach { enableMap.put(it, false) }

		// 校验
		for ((key, value) in enableMap) {
			if (!value && disableNotAllowedResponder.contains(key))
				throw ConfigurationError("CAN NOT disabled basic responder: `${key.javaClass.simpleName}`. Please:\n\t1. Remove this responder from entry `responders.disable` in file `config.json`.\n\t2. Add it into `responders.enable` in file`config.json`.\n\t3. Restart the program.")
		}
	}

	@JvmStatic
	fun main(args: Array<String>) {
		System.setProperty("file.encoding", "UTF-8")

		Runtime.getRuntime().addShutdownHook(MainServer.ShutdownHook())

		Configs.Companion.instance()
		RunningData.getInstance()
		Constants.Basic()
		Constants.Address()
		AvalonPluginPool.load()
		if (!Constants.Basic.DEBUG) {
			System.err.println("Debug not on! Exiting...")
			Runtime.getRuntime().halt(-1)
		}
		val scanner = Scanner(System.`in`)
		var id = 0L

		while (true) {
			print("Input here:")
			val content = scanner.nextLine()
			val message = GroupMessage(++id, System.currentTimeMillis(),
					DEBUG_MESSAGE_UID, "Test", DEBUG_MESSAGE_GROUP_UID, "Test Group", content)
			GroupMessageHandler.handle(message)
			println("===")
		}
	}

	fun addGroupMessageResponder(responder: GroupMessageResponder) {
		apiList.put(responder.responderInfo().keyWordRegex, responder)
		apiNameMap.put(responder.javaClass.simpleName, responder)
	}

	fun addCustomGroupResponder(responder: CustomGroupResponder) = customApiList.put(responder.getKeyWordRegex(), responder)

	fun setDisabledNotAllowed(responder: GroupMessageResponder) = disableNotAllowedResponder.add(responder)

	internal fun addBlackListPeople(first: Long, second: Int) = blacklistPeopleMap.put(first, second)
}