package avalon.api

import avalon.api.util.Plugin
import avalon.friend.FriendMessageResponder
import avalon.group.GroupMessageHandler
import avalon.group.GroupMessageResponder
import avalon.tool.pool.APISurvivePool

object RegisterResponder {
	@JvmStatic
	private val map = HashMap<Plugin, ArrayList<CustomGroupResponder>>()

	@JvmStatic
	fun register(responder: GroupMessageResponder) {
		GroupMessageHandler.addGroupMessageResponder(responder)
		APISurvivePool.getInstance().addAPI(responder)
	}

	fun register(plugin: Plugin, responder: CustomGroupResponder) {
		if (map.contains(plugin)) {
			val tempList = map[plugin]!!
			tempList.add(responder)
			map.replace(plugin, tempList)
		} else {
			val tempList = ArrayList<CustomGroupResponder>()
			tempList.add(responder)
			map.put(plugin, tempList)
		}
		GroupMessageHandler.addCustomGroupResponder(responder)
	}

	@JvmStatic
	fun register(responder: FriendMessageResponder): Unit =
			throw UnsupportedOperationException("register for FriendMessageResponder not finish yet :~)")

	@JvmStatic
	fun queryAvalonPlugin(plugin: Plugin): ArrayList<CustomGroupResponder> = map[plugin]!!
}