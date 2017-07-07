package avalon.main

import avalon.friend.FriendMessageResponder
import avalon.group.{GroupMessageHandler, GroupMessageResponder}
import avalon.tool.pool.APISurvivePool

/**
  * Created by Eldath Ray on 2017/4/19 0019.
  *
  * @author Eldath Ray
  */
object RegisterResponder {
  def register(responder: GroupMessageResponder): Unit = {
    GroupMessageHandler.addGroupMessageResponder(responder)
    APISurvivePool.getInstance.addAPI(responder)
  }

  def register(responder: FriendMessageResponder) =
    throw new UnsupportedOperationException("register for FriendMessageResponder not finish yet :~)")
}