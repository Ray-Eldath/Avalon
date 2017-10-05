package avalon.group

import avalon.util.BasicResponder
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.util.regex.Pattern

abstract class GroupMessageResponder : BasicResponder {
	abstract fun doPost(message: GroupMessage, groupConfig: GroupConfig)

	abstract override fun getHelpMessage(): String?

	abstract override fun getKeyWordRegex(): Pattern

	open fun permissionIdentifier(): Array<String>? = null

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		other as GroupMessageResponder
		return EqualsBuilder().append(other.helpMessage, helpMessage).append(other.keyWordRegex, keyWordRegex).isEquals
	}

	override fun hashCode(): Int = HashCodeBuilder(17, 37).append(helpMessage).append(keyWordRegex.toString()).toHashCode()
}