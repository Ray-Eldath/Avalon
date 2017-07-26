package avalon.group

import java.util.regex.Pattern

import avalon.util.{BasicResponder, GroupMessage}

/**
	* Created by Eldath on 2017/1/28 0028.
	*
	* @author Eldath
	*/
trait GroupMessageResponder extends BasicResponder {
	def doPost(message: GroupMessage): Unit

	override def getHelpMessage: String

	override def getKeyWordRegex: Pattern

	override def equals(`object`: Any): Boolean =
		`object` != null &&
			`object`.isInstanceOf[GroupMessageResponder] &&
			getKeyWordRegex.toString == `object`.asInstanceOf[GroupMessageResponder].getKeyWordRegex.toString

	def instance: GroupMessageResponder
}