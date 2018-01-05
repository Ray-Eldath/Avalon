package avalon.util.servlet

import avalon.tool.system.Configs
import avalon.util.FriendMessage
import avalon.util.GroupMessage
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.function.Consumer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object DiscordServlet : AvalonServlet() {
	private lateinit var groupMessageHook: Consumer<GroupMessage>
	private lateinit var friendMessageHook: Consumer<FriendMessage>

	private val obj = Configs.getJSONObject("servlet")

	override fun name(): String = "Discord"

	override fun responseGroup(groupUid: Long, reply: String) {
		jda.getTextChannelById(groupUid).sendMessage(reply)
	}

	override fun responseFriend(friendUid: Long, reply: String) {}

	override fun responsePrivate(uid: Long, reply: String) {
		jda.getUserById(uid).openPrivateChannel().complete().sendMessage(reply)
	}

	override fun shutUp(groupUid: Long, userUid: Long, time: Long) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun shutdown() {
		jda.shutdownNow()
	}

	override fun getGroupSenderNickname(groupUid: Long, userUid: Long): String =
			jda.getTextChannelById(groupUid).members.first { it.user.idLong == userUid }.effectiveName

	override fun getFriendSenderNickname(uid: Long): String =
			jda.getUserById(uid).name

	override fun setGroupMessageReceivedHook(hook: Consumer<GroupMessage>) {
		groupMessageHook = hook
	}

	override fun setFriendMessageReceivedHook(hook: Consumer<FriendMessage>) {
		friendMessageHook = hook
	}

	override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {}

	private val jda: JDA by lazy {
		JDABuilder(AccountType.BOT)
				.setToken(obj.getString("token"))
				.addEventListener(DiscordMessageListener(groupMessageHook))
				.buildBlocking()
	}

	private class DiscordMessageListener(val groupMessageHook: Consumer<GroupMessage>) : ListenerAdapter() {
		override fun onMessageReceived(event: MessageReceivedEvent?) {
			if (!event!!.isFromType(ChannelType.TEXT) || event.author.isBot)
				return
			val channel = event.textChannel
			val sender = event.author
			val message = event.message
			groupMessageHook.accept(GroupMessage(event.messageIdLong, message.creationTime.toLocalDateTime(), sender.idLong, sender.name, channel.idLong, channel.name, message.contentDisplay))
		}
	}
}