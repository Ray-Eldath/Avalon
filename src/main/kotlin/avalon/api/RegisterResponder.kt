package avalon.api

import avalon.friend.FriendMessageResponder
import avalon.group.GroupMessageHandler
import avalon.group.GroupMessageResponder
import avalon.tool.pool.APISurvivePool
import avalon.util.Plugin
import java.util.*

object RegisterResponder {
	@JvmStatic
	private val map = hashMapOf<Plugin, ArrayList<CustomGroupResponder>>() // 存储每个Plugin持有的CustomGroupResponder

	@Suppress("MemberVisibilityCanBePrivate")
	@JvmStatic
	fun register(responder: GroupMessageResponder) {
		val locale = responder.responderInfo().availableLocale
		if (locale != Locale.ROOT && locale != Locale.getDefault())
			return
		GroupMessageHandler.addGroupMessageResponder(responder)
		APISurvivePool.getInstance().addAPI(responder)
	}

	/**
	 * 内部方法。被设计为仅在Avalon内部可调用以强制所有外部功能均可以在配置文件中关闭。
	 */
	@JvmName(" {# THIS METHOD IS NOT DESIGNED FOR OUTER CALL #} ")
	@JvmSynthetic
	internal fun registerInner(responder: GroupMessageResponder, disableNotAllowed: Boolean = false) {
		register(responder)
		if (disableNotAllowed)
			GroupMessageHandler.setDisabledNotAllowed(responder)
	}

	@JvmStatic
	fun register(plugin: Plugin, responder: CustomGroupResponder) {
		if (plugin in map) {
			val tempList = map[plugin]!!
			tempList.add(responder)
			map.replace(plugin, tempList)
		} else {
			val tempList = arrayListOf<CustomGroupResponder>()
			tempList.add(responder)
			map[plugin] = tempList
		}
		GroupMessageHandler.addCustomGroupResponder(responder)
	}

	@JvmStatic
	fun register(responder: FriendMessageResponder): Nothing =
			throw UnsupportedOperationException("register for FriendMessageResponder not finish yet :~)")

	@JvmStatic
	fun queryAvalonPlugin(plugin: Plugin): ArrayList<CustomGroupResponder> = map[plugin]!!
}