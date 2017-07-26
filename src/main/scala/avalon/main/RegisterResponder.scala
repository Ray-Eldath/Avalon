package avalon.main

import avalon.api.CustomGroupResponder
import avalon.friend.FriendMessageResponder
import avalon.group.{GroupMessageHandler, GroupMessageResponder}
import avalon.tool.pool.APISurvivePool
import avalon.util.AvalonPlugin

import scala.collection.mutable

/**
	* Created by Eldath Ray on 2017/4/19 0019.
	*
	* @author Eldath Ray
	*/
object RegisterResponder {
	private val map = new mutable.HashMap[CustomGroupResponder, AvalonPlugin]

	def register(responder: GroupMessageResponder): Unit = {
		GroupMessageHandler.addGroupMessageResponder(responder)
		APISurvivePool.getInstance.addAPI(responder)
	}

	def register(plugin: AvalonPlugin, responder: CustomGroupResponder): Unit = {
		map.put(responder, plugin)
		GroupMessageHandler.addCustomGroupResponder(responder)
	}

	def register(responder: FriendMessageResponder) =
		throw new UnsupportedOperationException("register for FriendMessageResponder not finish yet :~)")

	def queryAvalonPlugin(responder: CustomGroupResponder): AvalonPlugin = map(responder)
}