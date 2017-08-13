package avalon.group

import java.util.regex.Pattern

import avalon.util.{GroupConfig, GroupMessage}

/**
	* Created by Eldath on 2017/1/29 0029.
	*
	* @author Eldath
	*/
object Echo extends GroupMessageResponder {
	override def doPost(message: GroupMessage, groupConfig: GroupConfig): Unit = {
		val content = message.getContent.replace("avalon echo ", "").replace("avalon repeat ", "").replace("阿瓦隆跟我说 ", "")
		message.response(content)
	}

	override def getHelpMessage = "avalon echo | avalon repeat | 阿瓦隆跟我说：让阿瓦隆重复给定语句"

	override def getKeyWordRegex: Pattern = Pattern.compile("avalon echo |avalon repeat |阿瓦隆跟我说 ")

	override def instance: GroupMessageResponder = this
}