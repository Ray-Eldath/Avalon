package avalon.util.backend

import avalon.tool.system.Configs
import avalon.util.FriendMessage
import avalon.util.GroupMessage
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import java.util.function.Consumer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Discord的Channel对应Avalon的Group。groupUid等均为Discord中Channel的ID。
 */
object DiscordBackend : AvalonBackend() {
	private val outerGroupMessageHook: Consumer<GroupMessage> = avalon.tool.pool.Constants.groupMessageReceivedHook
	private val outerFriendMessageHook: Consumer<FriendMessage> = avalon.tool.pool.Constants.friendMessageReceivedHook

	private val logger = LoggerFactory.getLogger(javaClass)

	private val obj = Configs.getJSONObject("backend")

	override fun name(): String = "Discord"

	override fun responseGroup(groupUid: Long, reply: String) {
		jda.getTextChannelById(groupUid).sendMessage(reply).complete()
	}

	override fun responseFriend(friendUid: Long, reply: String) {}

	override fun responsePrivate(uid: Long, reply: String) {
		jda.getUserById(uid).openPrivateChannel().complete().sendMessage(reply)
	}

	override fun shutUp(groupUid: Long, userUid: Long, time: Long) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun shutdown() = jda.shutdownNow()

	override fun getGroupSenderNickname(groupUid: Long, userUid: Long): String =
			jda.getTextChannelById(groupUid).members.first { it.user.idLong == userUid }.effectiveName

	override fun getFriendSenderNickname(uid: Long): String =
			jda.getUserById(uid).name

	override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {}

	private val jda: JDA =
			JDABuilder(AccountType.BOT)
					.setToken(obj.getString("token"))
					.addEventListener(DiscordMessageListener(outerGroupMessageHook, outerFriendMessageHook))
					.buildBlocking()

	class DiscordMessageListener(private val groupMessageHook: Consumer<GroupMessage>,
	                             private val friendMessageHook: Consumer<FriendMessage>) : ListenerAdapter() {
		override fun onMessageReceived(event: MessageReceivedEvent?) {
			if (!event!!.isFromType(ChannelType.TEXT) || event.author.isBot)
				return
			val sender = event.author
			val message = event.message
			if (event.isFromType(ChannelType.PRIVATE))
				friendMessageHook.accept(FriendMessage(
						event.messageIdLong,
						message.creationTime.toLocalDateTime(),
						sender.idLong,
						sender.name,
						message.contentDisplay))
			else {
				val channel = event.textChannel
				if (!channel.canTalk()) {
					logger.error("FATAL ERROR: Avalon does NOT have enough permission to talk in particular channel, exit.")
					System.exit(-1)
				}
				groupMessageHook.accept(GroupMessage(
						event.messageIdLong,
						message.creationTime.toLocalDateTime(), // TODO 时间不准。估计是由于时区问题。
						sender.idLong,
						sender.name,
						channel.idLong,
						channel.name,
						message.contentDisplay))
			}
		}
	}
}