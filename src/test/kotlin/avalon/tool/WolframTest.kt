package avalon.tool

import avalon.extend.WolframXMLParser
import avalon.tool.system.Config
import org.eclipse.jetty.util.UrlEncoded
import org.jdom2.input.SAXBuilder
import org.junit.Test
import java.net.URL

class WolframTest {
	@Test
	fun wolfram() {
		val question = "what is the tallest mountain in the world?"
		val url = URL("http://api.wolframalpha.com/v2/query?input=" + UrlEncoded.encodeString(question) + "&appid=" +
				Config.instance().getCommandConfig("Wolfram", "app_id"))
		val builder = SAXBuilder()
		val pods = WolframXMLParser.get(builder.build(url).rootElement)
		val builder1 = StringBuilder()
		for (thisPod in pods) {
			if (!thisPod.empty())
				builder1.append(thisPod.title()).append("\n").append("---\n").append(thisPod.plaintext())
		}
		builder1.append("\n\n详见：http://www.wolframalpha.com/input?i=").append(UrlEncoded.encodeString(question))

		println(builder1)
	}
}