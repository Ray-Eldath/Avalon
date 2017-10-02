package avalon.group

import java.io.IOException
import java.util.regex.Pattern

import avalon.extend.WolframXMLParser
import avalon.tool.pool.ConstantPool.Basic.currentServlet
import avalon.tool.system.Config
import avalon.util.{GroupConfig, GroupMessage}
import org.eclipse.jetty.util.UrlEncoded
import org.jdom2.JDOMException
import org.jdom2.input.SAXBuilder
import org.slf4j.LoggerFactory

/**
	* Created by Eldath Ray on 2017/6/11 0011.
	*
	* @author Eldath Ray
	*/
object Wolfram extends GroupMessageResponder {
	override def doPost(message: GroupMessage, groupConfig: GroupConfig): Unit = {
		val content = message.getContent
		val question = content.replace("avalon tell me ", "")
		val url = "http://api.wolframalpha.com/v2/query?input=" + UrlEncoded.encodeString(question) + "&appid=" +
			Config.instance.getCommandConfig("Wolfram", "app_id")
		if (message.getContent.matches("avalon tell me [\\u4e00-\\u9fa5]")) {
			message.response(" 指令不合规范~ o(╯□╰)o")
			return
		}
		message.response(avalon.api.Flag.AT(message) + " 由于消息长度过长，将会将结果私聊给您。请等待网络延迟！^_^#")
		try {
			val builder = new SAXBuilder
			val pods = WolframXMLParser.get(builder.build(url).getRootElement)
			val builder1 = new StringBuilder
			for (thisPod <- pods)
				builder1.append(thisPod).append("\n").append("---\n").append(thisPod.plaintext)
			builder1.append("\n详见：http://www.wolframalpha.com/input?i=").append(UrlEncoded.encodeString(question))
			currentServlet.responseFriend(message.getSenderUid, builder1.toString)
		} catch {
			case e@(_: JDOMException | _: IOException) =>
				LoggerFactory.getLogger(Wolfram.this.getClass)
					.error("exception thrown while parse XML from " + url + " " + e.toString)
		}
	}

	override def getHelpMessage = "avalon tell me <your question>: (Only English) send your question to Wolfram Alpha and echo the return."

	override def getKeyWordRegex: Pattern = Pattern.compile("avalon tell me \\w+")

	override def instance: GroupMessageResponder = this
}