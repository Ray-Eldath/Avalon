package avalon.group

import java.util.regex.Pattern

import avalon.util.{BasicResponder, GroupConfig, GroupMessage}
import org.apache.commons.lang3.builder.{EqualsBuilder, HashCodeBuilder}

/**
	* Created by Eldath on 2017/1/28 0028.
	*
	* @author Eldath
	*/
trait GroupMessageResponder extends BasicResponder {
	def doPost(message: GroupMessage, groupConfig: GroupConfig): Unit

	override def getHelpMessage: String

	override def getKeyWordRegex: Pattern

	def permissionIdentifier: Array[String] = null

	override def equals(obj: scala.Any): Boolean = {
		if (obj == null || (!obj.isInstanceOf[GroupMessageResponder])) return false
		val that = obj.asInstanceOf[GroupMessageResponder]
		new EqualsBuilder()
			.append(getKeyWordRegex, that.getKeyWordRegex)
			.append(permissionIdentifier, that.permissionIdentifier)
			.isEquals
	}

	override def hashCode(): Int =
		new HashCodeBuilder(17, 37)
			.append(getHelpMessage)
			.append(getKeyWordRegex)
			.append(permissionIdentifier)
			.hashCode()

	def instance: GroupMessageResponder
}