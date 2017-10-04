package avalon.extend

import org.jdom2.Element
import org.jdom2.xpath.XPathFactory

/**
 * Created by Eldath Ray on 2017/10/5 0011.
 *
 * @author Eldath Ray
 */
object WolframXMLParser {
	class WolframPod(val title: String, val id: String, val plaintext: String) {
		fun empty(): Boolean = title.isEmpty() && plaintext.isEmpty()
	}

	fun get(root: Element): List<WolframPod> {
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