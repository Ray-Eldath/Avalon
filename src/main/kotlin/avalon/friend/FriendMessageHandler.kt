package avalon.friend

import avalon.extend.Recorder
import avalon.tool.pool.Constants
import avalon.util.FriendMessage

object FriendMessageHandler {
	fun handle(message: FriendMessage) {
		if (!Constants.Basic.debug)
			Recorder.getInstance().recodeFriendMessage(message)
	}
}