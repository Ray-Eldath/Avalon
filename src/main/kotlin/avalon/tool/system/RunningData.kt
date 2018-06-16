package avalon.tool.system

import org.json.JSONObject
import org.json.JSONTokener
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

/**
 * @author Ray Eldath
 * @since v1.2.3
 * 全功能测试通过。2018.2.8
 */
object RunningData : Closeable {
	private val logger = LoggerFactory.getLogger(RunningData::class.java)

	private var jsonObject: JSONObject? = null
	private var empty: Boolean = true
	private lateinit var statement: Statement

	private val default = JSONObject().let {
		it.put("group_id", 0)
		it.put("friend_id", 0)
	}

	init {
		try {
			Class.forName("org.h2.Driver")
			statement = DriverManager.getConnection("jdbc:h2:./data/data;IFEXISTS=TRUE").createStatement()
			val resultSet = statement.executeQuery("SELECT * FROM DATA_")
			resultSet.next()
			val any = JSONTokener(resultSet.getString("data")).nextValue()
			// init
			empty = any == JSONObject.NULL
			jsonObject = if (empty) default else any as JSONObject
		} catch (e: Exception) {
			logger.error("fatal error while load H2 database driver: `${e.localizedMessage}`")
			Runtime.getRuntime().halt(-1)
		}
	}

	fun save() {
		try {
			statement.execute("UPDATE data_ SET data='${jsonObject.toString()}'")
		} catch (e: SQLException) {
			logger.warn("exception thrown while writing running data: `${e.localizedMessage}`")
		}
	}

	fun set(key: String, value: Any) = jsonObject!!.put(key, value)!!

	fun get(key: String) = jsonObject!!.get(key)!!

	override fun close() = statement.close()
}