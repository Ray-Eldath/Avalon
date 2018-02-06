package avalon.tool.pool

import avalon.friend.FriendMessageHandler
import avalon.group.GroupMessageHandler
import avalon.tool.database.MySQLDatabaseOperator
import avalon.tool.database.SQLiteDatabaseOperator
import avalon.tool.system.Configs
import avalon.util.FriendMessage
import avalon.util.GroupMessage
import avalon.util.backend.AvalonBackend
import avalon.util.backend.CoolQBackend
import avalon.util.backend.DiscordBackend
import java.io.File
import java.lang.management.ManagementFactory
import java.util.function.Consumer

/**
 * Created by Eldath on 2017/2/3 0003.
 *
 * @author Eldath
 */
object Constants {

	object Database {
		val DATASOURCE = Configs.Companion.instance().getJSONObject("database").getString("datasource").toLowerCase()
		val CURRENT_DATABASE_OPERATOR =
				(if ("mysql" == DATASOURCE)
					MySQLDatabaseOperator.getInstance()
				else
					SQLiteDatabaseOperator.getInstance())!!
	}

	object Address {
		val SERVLET = Basic.CURRENT_SERVLET.apiAddress()
		val SERVLET_SCRIPT_FILE = Basic.CURRENT_SERVLET.scriptFilePath()
		val PERL_FILE_OF_WECHAT = Basic.CURRENT_PATH + File.separator + "bin" + File.separator + "Mojo-Weixin.pl"
		val DATA_PATH = Basic.CURRENT_PATH + File.separator + "data"
	}

	object Version {
		const val AVALON = "1.2.3"
		val SERVLET = Basic.CURRENT_SERVLET.version()
	}

	object Basic {
		val CURRENT_SERVLET: AvalonBackend =
				when (Configs.Companion.instance().getJSONObject("backend").getString("backend").trim { it <= ' ' }.toLowerCase()) {
					"coolq" -> CoolQBackend.INSTANCE()
					"discord" -> DiscordBackend
					else -> null!!
				}

		val DEFAULT_PREFIX = arrayOf("avalon ")
		val LOCAL_OUTPUT = Configs.Companion.instance().get("local_output") as Boolean
		val DEBUG = Configs.Companion.instance().get("debug") as Boolean
		val START_TIME = System.currentTimeMillis()
		const val DEBUG_MESSAGE_UID: Long = 10000
		const val DEBUG_MESSAGE_GROUP_UID: Long = 11111
		val PID = Integer.parseInt(ManagementFactory.getRuntimeMXBean().name.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
		const val MAX_ECHO_LENGTH = 100
		val CURRENT_PATH: String = File("").canonicalPath
	}

	object Setting {
		val Block_Words_Punishment_Mode_Enabled = Configs.Companion.instance().get("block_words_punishment_mode_enabled") as Boolean
		val RSS_Enabled = Configs.isPluginEnable("RSS")
		val BuildStatus_Enabled = Configs.isPluginEnable("BuildStatus")
	}

	@JvmStatic
	val groupMessageReceivedHook = Consumer<GroupMessage> { GroupMessageHandler.handle(it) }

	@JvmStatic
	val friendMessageReceivedHook = Consumer<FriendMessage> { FriendMessageHandler.handle(it) }
}