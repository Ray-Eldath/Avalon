package avalon.api

import avalon.util.DelayMessage
import avalon.util.Message

import java.util.concurrent.DelayQueue

/**
 * Created by Eldath Ray on 2017/3/22.
 *
 * @author Eldath Ray
 */
class DelayResponse : Thread() {
	private val messages = DelayQueue<DelayMessage>()

	fun delay(message: DelayMessage): Boolean = messages.offer(message)

	override fun run() {
		var delayMessage: DelayMessage? = messages.poll()
		var message: Message
		while (delayMessage != null) {
			message = delayMessage.message
			message.response(delayMessage.doWhat.invoke())
			delayMessage = messages.poll()
		}
	}
}
