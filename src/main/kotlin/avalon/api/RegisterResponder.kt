package avalon.api

import avalon.api.util.Plugin
import avalon.friend.FriendMessageResponder
import avalon.group.GroupMessageHandler
import avalon.group.GroupMessageResponder
import avalon.tool.pool.APISurvivePool

object RegisterResponder {
	@JvmStatic
	private val map = hashMapOf<Plugin, ArrayList<CustomGroupResponder>>()

	@JvmStatic
	fun register(responder: GroupMessageResponder) {
		GroupMessageHandler.addGroupMessageResponder(responder)
		APISurvivePool.getInstance().addAPI(responder)
	}

	fun register(plugin: Plugin, responder: CustomGroupResponder) {
		if (plugin in map) {
			val tempList = map[plugin]!!
			tempList.add(responder)
			map.replace(plugin, tempList)
		} else {
			val tempList = arrayListOf<CustomGroupResponder>()
			tempList.add(responder)
			map.put(plugin, tempList)
		}
		GroupMessageHandler.addCustomGroupResponder(responder)
	}

	@JvmStatic
	fun register(responder: FriendMessageResponder): Nothing =
			throw UnsupportedOperationException("register for FriendMessageResponder not finish yet :~)")

	@JvmStatic
	fun queryAvalonPlugin(plugin: Plugin): ArrayList<CustomGroupResponder> = map[plugin]!!
}