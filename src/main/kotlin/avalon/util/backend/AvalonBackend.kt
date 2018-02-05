package avalon.util.backend

import avalon.util.Service
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URL
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Eldath Ray on 2017/6/9 0009.
 *
 * @author Eldath Ray
 */
abstract class AvalonBackend : HttpServlet(), Service {

	abstract fun name(): String

	open fun version(): String = "UNKNOWN"

	open fun scriptFilePath(): String = ""

	open fun apiAddress(): String = ""

	open fun listenAddress(): String = ""

	open fun servlet() = false

	override fun available(): Boolean =
			if (!servlet())
				true
			else
				try {
					URL(apiAddress()).openConnection().connect()
					true
				} catch (e: Exception) {
					logger.error("exception thrown while testing usability of backend: `" + e.localizedMessage + "`")
					false
				}

	abstract fun responseGroup(groupUid: Long, reply: String)

	abstract fun responseFriend(friendUid: Long, reply: String)

	abstract fun responsePrivate(uid: Long, reply: String)

	abstract fun shutUp(groupUid: Long, userUid: Long, time: Long)

	open fun shutdown() {
		logger.info("please shutdown ${name()} service manually")
	}

	open fun reboot() {
		logger.info("please reboot ${name()} service manually")
	}

	open fun clean() {
		logger.info("No clean needed.")
	}

	abstract fun getGroupSenderNickname(groupUid: Long, userUid: Long): String

	abstract fun getFriendSenderNickname(uid: Long): String

	@Throws(ServletException::class, IOException::class)
	public abstract override fun doPost(req: HttpServletRequest, resp: HttpServletResponse)

	companion object {
		private val logger = LoggerFactory.getLogger(AvalonBackend::class.java)
	}
}
