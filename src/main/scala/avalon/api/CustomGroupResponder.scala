package avalon.api

import java.util.regex.Pattern

import avalon.util.GroupMessage

trait CustomGroupResponder {
	def doPost(message: GroupMessage): Unit

	def getHelpMessages: List[String]

	def getKeyWordRegex: Pattern

	override def equals(`object`: Any): Boolean =
		`object` != null &&
			`object`.isInstanceOf[CustomGroupResponder] &&
			getKeyWordRegex.toString.equals(`object`.asInstanceOf[CustomGroupResponder].getKeyWordRegex.toString)
}
