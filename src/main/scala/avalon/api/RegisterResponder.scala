package avalon.api

import avalon.api.util.Plugin
import avalon.friend.FriendMessageResponder
import avalon.group.{GroupMessageHandler, GroupMessageResponder}
import avalon.tool.pool.APISurvivePool

import scala.collection.mutable

/**
	* Created by Eldath Ray on 2017/4/19 0019.
	*
	* @author Eldath Ray
	*/
object RegisterResponder {
	private val map = new mutable.HashMap[Plugin, java.util.ArrayList[CustomGroupResponder]]

	def register(responder: GroupMessageResponder): Unit = {
		GroupMessageHandler.addGroupMessageResponder(responder)
		APISurvivePool.getInstance.addAPI(responder)
	}

	def register(plugin: Plugin, responder: CustomGroupResponder): Unit = {
		if (map.contains(plugin)) {
			val tempList = map(plugin)
			tempList.add(responder)
			map.update(plugin, tempList)
		} else {
			val tempList = new java.util.ArrayList[CustomGroupResponder]()
			tempList.add(responder)
			map.put(plugin, tempList)
		}
		GroupMessageHandler.addCustomGroupResponder(responder)
	}

	def register(responder: FriendMessageResponder) =
		throw new UnsupportedOperationException("register for FriendMessageResponder not finish yet :~)")

	def queryAvalonPlugin(plugin: Plugin): java.util.ArrayList[CustomGroupResponder] = map(plugin)
}