package avalon.group

import java.util.regex.Pattern

import avalon.util.{GroupConfig, GroupMessage}

/**
	* Created by Eldath on 2017/3/11 0011.
	*
	* @author Eldath Ray
	*/
object Test extends GroupMessageResponder {

	override def doPost(message: GroupMessage, groupConfig: GroupConfig): Unit = {
	}

	override def getHelpMessage = ""

	override def getKeyWordRegex: Pattern = Pattern.compile("avalon test group")

	override def instance: GroupMessageResponder = this
}