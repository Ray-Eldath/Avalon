package avalon.friend

import avalon.plugin.Recorder
import avalon.tool.pool.Constants
import avalon.util.FriendMessage

object FriendMessageHandler {
	fun handle(message: FriendMessage) {
		if (!Constants.Basic.DEBUG)
			Recorder.getInstance().recodeFriendMessage(message)
	}
}