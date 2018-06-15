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
	private val statement: Statement
	private var jsonObject: JSONObject? = null
	private val jsonObjectString: String
	private val empty: Boolean

	private val default = JSONObject().let {
		it.put("group_id", 0)
		it.put("friend_id", 0)
	}

	init {
		Class.forName("org.h2.Driver")
		statement = DriverManager.getConnection("jdbc:h2:./data/data.h2;IFEXISTS=TRUE").createStatement()
		val resultSet = statement.executeQuery("SELECT * FROM DATA_")
		resultSet.next()
		jsonObjectString = resultSet.getString("data")
		// init
		empty = jsonObjectString.isEmpty()
	}

	fun save() {
		try {
			statement.execute("UPDATE data_ SET data='${jsonObject!!}'")
		} catch (e: SQLException) {
			logger.warn("exception thrown while writing running data: `${e.localizedMessage}`")
		}
	}

	fun set(key: String, value: Any) {
		read()
		jsonObject!!.put(key, value)
	}

	fun read(): JSONObject =
			jsonObject ?: (if (empty) default else JSONTokener(jsonObjectString).nextValue() as JSONObject)
					.also { jsonObject = it }

	override fun close() {
		statement.close()
	}
}