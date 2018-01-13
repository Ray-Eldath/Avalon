package test

import org.slf4j.LoggerFactory

object ColorConsoleTest {

	@JvmStatic
	fun main(args: Array<String>) {
		val logger = LoggerFactory.getLogger(this.javaClass)
		logger.debug("debug")
		logger.info("info")
		logger.info("info")
		logger.info("info")
		logger.warn("warn")
		logger.warn("warn")
		logger.error("error")
		logger.error("error")
	}
}