package avalon.friend

import avalon.function.Recorder
import avalon.tool.pool.Constants
import avalon.tool.pool.Constants.Basic.LANG
import avalon.util.FriendMessage

object FriendMessageHandler {
	fun handle(message: FriendMessage) {
		if (!Constants.Basic.DEBUG)
			Recorder.getInstance().recodeFriendMessage(message)
		message.response(LANG.getString("friend.handler.reply"))
	}
}