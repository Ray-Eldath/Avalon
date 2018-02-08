package avalon.tool.database

import avalon.tool.system.RunningData
import avalon.util.FriendMessage
import avalon.util.GroupMessage
import avalon.util.Message
import org.eclipse.jetty.util.UrlEncoded
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement
import java.time.format.DateTimeFormatter

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
class BasicDatabaseOperator(private val connection: Connection) : Closeable {
	companion object {
		private val logger = LoggerFactory.getLogger(BasicDatabaseOperator::class.java)
		private val groupId = RunningData.read().getInt("group_id")
		private val friendId = RunningData.read().getInt("friend_id")

		private lateinit var addGroupMessage: PreparedStatement
		private lateinit var addFriendMessage: PreparedStatement
		private lateinit var addQuote: PreparedStatement
	}

	init {
		try {
			addGroupMessage = connection.prepareStatement(
					"INSERT INTO group_ (time, senderUid, senderNickName, groupUid, groupName, content) VALUES (?,?,?,?,?,?)")
			addFriendMessage = connection.prepareStatement(
					"INSERT INTO friend_ (time, senderUid, senderNickname, content) VALUES (?,?,?,?)")
			addQuote = connection.prepareStatement(
					"INSERT INTO quote_ (uid, speaker, content) VALUES (?,?,?)")
		} catch (e: SQLException) {
			logger.error("error while init database connection: `${e.localizedMessage}`")
		}
	}

	fun add(message: GroupMessage): Boolean {
		return try {
			addGroupMessage.clearParameters()

			addGroupMessage.setObject(1, time(message))
			addGroupMessage.setLong(2, message.senderUid)
			addGroupMessage.setString(3, UrlEncoded.encodeString(message.senderNickName))
			addGroupMessage.setLong(4, message.groupUid)
			addGroupMessage.setString(5, UrlEncoded.encodeString(message.groupName))
			addGroupMessage.setString(6, UrlEncoded.encodeString(message.content))

			addGroupMessage.execute()
		} catch (e: SQLException) {
			logger.warn("error while saving group message to database: `" + e.localizedMessage + "`")
			false
		}
	}

	fun add(message: FriendMessage): Boolean {
		return try {
			addFriendMessage.clearParameters()

			addFriendMessage.setObject(1, time(message))
			addFriendMessage.setLong(2, message.senderUid)
			addFriendMessage.setString(3, UrlEncoded.encodeString(message.senderNickName))
			addFriendMessage.setString(4, UrlEncoded.encodeString(message.content))

			addFriendMessage.execute()
		} catch (e: SQLException) {
			logger.warn("error while saving friend message to SQLite: `" + e.localizedMessage + "`")
			false
		}

	}

	fun addQuote(hashCode: Int, speaker: String, content: String): Boolean {
		return try {
			addQuote.clearParameters()

			addQuote.setInt(1, hashCode)
			addQuote.setString(2, UrlEncoded.encodeString(speaker))
			addQuote.setString(3, UrlEncoded.encodeString(content))

			addQuote.execute()
		} catch (e: SQLException) {
			logger.warn("error while saving quote to SQLite: `${e.localizedMessage}`")
			false
		}
	}

	fun exist(statement: Statement, table: Table, condition: String): Boolean {
		return try {
			val set = statement.executeQuery(String.format("SELECT COUNT(*) FROM %s WHERE %s", fromTable(table), condition))
			set.getInt(1) != 0
		} catch (e: SQLException) {
			logger.warn("error while questing existence of table ${table.name}: `${e.localizedMessage}`")
			false
		}
	}

	fun count(statement: Statement, table: Table): Int {
		return try {
			statement.executeQuery(String.format("SELECT COUNT(1) FROM %s", fromTable(table))).getInt(0)
		} catch (e: SQLException) {
			logger.warn("error while questing count of table ${table.name}: `${e.localizedMessage}`")
			-1
		}
	}

	override fun close() {
		connection.close()
		RunningData.set("group_id", groupId + 1)
		RunningData.set("friend_id", friendId + 1)
		RunningData.save()
	}

	private fun time(message: Message): String =
			message.time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ")

	private fun fromTable(table: Table): String? =
			when (table) {
				Table.FRIEND -> "friend_"
				Table.GROUP -> "group_"
				Table.QUOTE -> "quote_"
			}
}

