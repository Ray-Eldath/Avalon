package avalon.tool.pool

import avalon.friend.FriendMessageHandler
import avalon.group.GroupMessageHandler
import avalon.tool.database.H2DatabaseOperator
import avalon.tool.database.MySQLDatabaseOperator
import avalon.tool.system.Configs
import avalon.util.ConfigurationError
import avalon.util.FriendMessage
import avalon.util.GroupMessage
import avalon.util.backend.AvalonBackend
import avalon.util.backend.CoolQBackend
import avalon.util.backend.DiscordBackend
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.management.ManagementFactory
import java.util.*
import java.util.function.Consumer

/**
 * Created by Ray Eldath on 2017/2/3 0003.
 *
 * @author Ray Eldath
 */
object Constants {
	private val logger = LoggerFactory.getLogger(this.javaClass)

	object Database {
		private val datasource = Configs.getJSONObject("database").getString("datasource").toLowerCase()
		val DB_OPERATOR =
				when (datasource) {
					"mysql" -> MySQLDatabaseOperator.getInstance()
					else -> H2DatabaseOperator
				}
	}

	object Address {
		val SERVLET = Basic.CURRENT_SERVLET.apiAddress()
		val SERVLET_SCRIPT_FILE = Basic.CURRENT_SERVLET.scriptFilePath()
		val PERL_FILE_OF_WECHAT = Basic.CURRENT_PATH + File.separator + "bin" + File.separator + "Mojo-Weixin.pl"
		val DATA_PATH = Basic.CURRENT_PATH + File.separator + "data"
	}

	object Version {
		const val AVALON = "1.3.0"
		val SERVLET = Basic.CURRENT_SERVLET.version()
	}

	object Basic {
		init {
			if (Configs.has("language")) {
				val languageString = Configs.getString("language")
				if (languageString.contains("_")) {
					languageString.split("_").let { Locale.setDefault(Locale(it[0], it[1])) }
				} else
					Locale.setDefault(Locale(languageString))
			}
		}

		val LANG = ResourceBundle.getBundle("lang.Avalon")!!
		val CURRENT_SERVLET: AvalonBackend =
				when (Configs.getJSONObject("backend").getString("backend").trim { it <= ' ' }.toLowerCase()) {
					"coolq" -> CoolQBackend
					"discord" -> DiscordBackend
					else -> {
						val string = "invalid configuration: only `CoolQ` and `Discord` backend are supported!"
						logger.error(string)
						throw ConfigurationError(string)
						null!!
					}
				}

		val DEFAULT_PREFIX = arrayOf("avalon ")
		val LOCAL_OUTPUT = Configs.get("local_output") as Boolean
		val DEBUG = {
			val forceDebug = System.getProperty("avalon.force_debug")
			if (forceDebug != null && forceDebug.equals("true", true)) {
				logger.warn("debug forced enabled!")
				true
			} else
				Configs.get("debug") as Boolean
		}.invoke()

		val START_TIME = System.currentTimeMillis()
		const val DEBUG_MESSAGE_UID: Long = 10000
		const val DEBUG_MESSAGE_GROUP_UID: Long = 11111
		val PID = Integer.parseInt(ManagementFactory.getRuntimeMXBean().name.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
		const val MAX_ECHO_LENGTH = 100
		val CURRENT_PATH: String = File("").canonicalPath
	}

	object Setting {
		val Block_Words_Punishment_Mode_Enabled = Configs.get("block_words_punishment_mode_enabled") as Boolean
		val RSS_Enabled = Configs.isPluginEnable("RSS")
		val BuildStatus_Enabled = Configs.isPluginEnable("BuildStatus")
	}

	@JvmStatic
	val groupMessageReceivedHook = Consumer<GroupMessage> { GroupMessageHandler.handle(it) }

	@JvmStatic
	val friendMessageReceivedHook = Consumer<FriendMessage> { FriendMessageHandler.handle(it) }
}