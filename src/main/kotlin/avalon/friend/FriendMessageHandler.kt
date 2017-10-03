package avalon.friend

import avalon.extend.Recorder
import avalon.tool.pool.ConstantPool
import avalon.util.FriendMessage

object FriendMessageHandler {
	fun handle(message: FriendMessage) {
		if (!ConstantPool.Basic.debug)
			Recorder.getInstance().recodeFriendMessage(message)
	}
}