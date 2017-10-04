package avalon.api

import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.util.regex.Pattern

abstract class CustomGroupResponder {
	abstract fun doPost(message: GroupMessage, groupConfig: GroupConfig)

	abstract fun getHelpMessage(): String

	abstract fun getKeyWordRegex(): Pattern

	override fun hashCode() = HashCodeBuilder(17, 37).toHashCode()
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		other as CustomGroupResponder
		return EqualsBuilder().append(other.getKeyWordRegex().toString(), getKeyWordRegex().toString()).isEquals
	}
}