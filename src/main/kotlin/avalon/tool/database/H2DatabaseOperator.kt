package avalon.tool.database

import avalon.tool.pool.Constants
import avalon.util.FriendMessage
import avalon.util.GroupMessage
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.DriverManager
import java.sql.Statement

/**
 * @author Ray Eldath
 * @since v1.2.3
 * 全功能测试通过。2018.2.8
 */
object H2DatabaseOperator : DatabaseOperator {
	private val logger = LoggerFactory.getLogger(H2DatabaseOperator.javaClass)
	private lateinit var operator: BasicDatabaseOperator
	private lateinit var statement: Statement

	private val fileS = Constants.Basic.CURRENT_PATH + "/res/record.h2.mv.db"

	init {
		try {
			if (!dbFileExist()) {
				logger.info("H2 database not exist. Try to initialize database...")
				if (initDB())
					logger.info("Successfully initialize database.")
				else
					logger.error("Failed to initialize database. Please try to initialize yourself.")
			}
			Class.forName("org.h2.Driver")
			val connection = DriverManager.getConnection("jdbc:h2:./res/record.h2;IFEXISTS=TRUE")
			operator = BasicDatabaseOperator(connection)
			statement = connection.createStatement()
		} catch (e: Exception) {
			logger.error("fatal error while load H2 database driver: `${e.localizedMessage}`")
			Runtime.getRuntime().halt(-1)
		}
	}

	override fun initDB(): Boolean {
		val emptyFileS = Constants.Basic.CURRENT_PATH + "/res/EMPTY_record.h2.mv.db"
		if (dbFileExist())
			return true
		Files.copy(Paths.get(emptyFileS), Paths.get(fileS))
		return true
	}

	private fun dbFileExist(): Boolean = Files.exists(Paths.get(fileS))

	override fun add(input: GroupMessage): Boolean = operator.add(input)

	override fun add(input: FriendMessage): Boolean = operator.add(input)

	override fun addQuote(hashCode: Int, speaker: String, content: String): Boolean = operator.addQuote(hashCode, speaker, content)

	override fun exist(table: Table, condition: String): Boolean = operator.exist(statement, table, condition)

	override fun count(table: Table): Int = operator.count(statement, table)

	override fun close() {
		operator.close()
		statement.close()
	}

	override fun statement(): Statement = statement
}