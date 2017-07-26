package avalon.friend

import avalon.extend.Recorder
import avalon.util.FriendMessage

/**
	* Created by Eldath Ray on 2017/4/1 0001.
	*
	* @author Eldath Ray
	*/
object FriendMessageHandler {
	def handle(message: FriendMessage): Unit = {
		// if (MessageChecker.checkEncode(message)) return;
		Recorder.getInstance.recodeFriendMessage(message)
	}
}