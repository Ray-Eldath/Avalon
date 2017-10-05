package avalon.util

import java.util.regex.Pattern

interface BasicResponder : BaseResponder {
	fun getHelpMessage(): String?

	override fun getKeyWordRegex(): Pattern
}

interface BaseResponder {
	fun getKeyWordRegex(): Pattern
}

interface Displayable {
	fun getString(): String
}