package avalon.friend

import avalon.function.Recorder
import avalon.tool.pool.Constants
import avalon.util.FriendMessage

object FriendMessageHandler {
	fun handle(message: FriendMessage) {
		if (!Constants.Basic.DEBUG)
			Recorder.getInstance().recodeFriendMessage(message)
		message.response("Avalon暂不支持私聊消息。请在监听的群中发送指令。")
	}
}