package avalon.backend

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder

object DiscordBackendTest {
	@JvmStatic
	fun main(args: Array<String>) {
		val jda: JDA =
				JDABuilder(AccountType.BOT)
						.setToken("NDA5MDE3Mjg2MDQxNzk2NjI4.DVYelw.tyl0aZrKSRdvuMwOfjKSoy163V4")
						.buildBlocking()
		val textChannel = jda.getTextChannelById(409010486450061324)
		textChannel.sendMessage("${jda.getUserById(391204494018478081).asMention} alpha!").submit()
	}
}