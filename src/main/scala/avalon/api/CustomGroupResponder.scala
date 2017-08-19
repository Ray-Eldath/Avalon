package avalon.api

import java.util.regex.Pattern

import avalon.util.{GroupConfig, GroupMessage}
import org.apache.commons.lang3.builder.{EqualsBuilder, HashCodeBuilder}

trait CustomGroupResponder {
	def doPost(message: GroupMessage, groupConfig: GroupConfig): Unit

	def getHelpMessages: List[String]

	def getKeyWordRegex: Pattern

	override def equals(obj: scala.Any): Boolean = {
		if (obj == null || (!obj.isInstanceOf[CustomGroupResponder])) return false
		val that = obj.asInstanceOf[CustomGroupResponder]
		new EqualsBuilder()
			.append(getKeyWordRegex, that.getKeyWordRegex)
			.isEquals
	}

	override def hashCode(): Int =
		new HashCodeBuilder(17, 37).append(getKeyWordRegex).hashCode()
}
