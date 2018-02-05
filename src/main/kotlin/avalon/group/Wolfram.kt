package avalon.group

import avalon.api.Flag
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.tool.system.Configs
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.eclipse.jetty.util.UrlEncoded
import org.jdom2.Element
import org.jdom2.JDOMException
import org.jdom2.input.SAXBuilder
import org.jdom2.xpath.XPathFactory
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

object Wolfram : GroupMessageResponder() {
	private val LOGGER = LoggerFactory.getLogger(this.javaClass)

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val content = message.content
		val question = content.replace("avalon tell me ", "")
		val url = "http://api.wolframalpha.com/v2/query?input=" + UrlEncoded.encodeString(question) + "&appid=" +
				Configs.getResponderConfig("Wolfram", "app_id")
		if (message.content.matches(Regex("avalon tell me [\\u4e00-\\u9fa5]"))) {
			message.response("${Flag.AT(message)} 不支持中文输入~ o(╯□╰)o")
			return
		}
		message.response(avalon.api.Flag.AT(message) + " 由于消息长度过长，将会将结果私聊给您。请等待网络延迟！^_^#")
		try {
			val builder = SAXBuilder()
			val pods = WolframXMLParser.get(builder.build(URL(url).openStream().reader(StandardCharsets.UTF_8)).rootElement)
			val builder1 = StringBuilder()
			pods.filterNot { it.empty() }
					.forEach { builder1.append(it.title).append("\n").append("---\n").append(it.plaintext) }
			builder1.append("\n\n详见：http://www.wolframalpha.com/input?i=").append(UrlEncoded.encodeString(question))
			CURRENT_SERVLET.responsePrivate(message.senderUid, builder1.toString())
		} catch (e: JDOMException) {
			LOGGER.error("exception thrown while parse XML from $url $e")
		} catch (e: IOException) {
			LOGGER.error("exception thrown while parse XML from $url $e")
		}
	}

	override fun responderInfo(): ResponderInfo = ResponderInfo(
			Pair("tell me <your question>", "(Only English) send your question to Wolfram Alpha and echo the return."),
			Pattern.compile("tell me \\w+")
	)

	override fun instance() = this
}

private object WolframXMLParser {
	internal class WolframPod(val title: String, val id: String, val plaintext: String) {
		fun empty(): Boolean = title.isEmpty() && plaintext.isEmpty()
	}

	internal fun get(root: Element): List<WolframPod> {
		val xPath = XPathFactory.instance()
		val objects = xPath.compile("//subpod").diagnose(root, false)
		val result = ArrayList<WolframPod>()
		objects.result.forEach({
			val e1 = it as Element
			result += WolframPod(
					handleString(e1.getAttributeValue("title")),
					handleString(e1.getAttributeValue("id")),
					handleString(e1.getChild("plaintext").value))
		})
		return result
	}

	private fun handleString(i: String?): String {
		if (i == null)
			return ""
		return i.replace(" +", " ")
	}
}