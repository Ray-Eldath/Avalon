package avalon.util

import java.util.regex.Pattern

trait BasicResponder extends BaseResponder {
	def getHelpMessage: String

	override def getKeyWordRegex: Pattern
}
