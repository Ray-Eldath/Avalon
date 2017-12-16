package avalon.group

import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.util.regex.Pattern

abstract class GroupMessageResponder {
	abstract fun doPost(message: GroupMessage, groupConfig: GroupConfig)

	abstract fun instance(): GroupMessageResponder?

	abstract fun responderInfo(): ResponderInfo

	override fun equals(other: Any?): Boolean {
		if (this === other || other === null) return true
		if (javaClass != other.javaClass) return false
		other as GroupMessageResponder
		val info = other.responderInfo()
		return EqualsBuilder()
				.append(info.helpMessage, responderInfo().helpMessage)
				.append(info.keyWordRegex, responderInfo().keyWordRegex).isEquals
	}

	override fun hashCode(): Int =
			HashCodeBuilder(17, 37)
					.append(responderInfo().helpMessage)
					.append(responderInfo().keyWordRegex.toString()).toHashCode()
}

enum class ResponderPermission {
	ALL, ADMIN, OWNER
}

class ResponderInfo(val helpMessage: Pair<String, String>,
                    val keyWordRegex: Pattern,
                    val configIdentifier: Array<String>? = null,
                    val manageable: Boolean = true,
                    val permission: ResponderPermission = ResponderPermission.ALL)