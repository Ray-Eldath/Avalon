package avalon.extend

import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util

import org.apache.commons.lang3.builder.HashCodeBuilder
import org.jdom2.Element
import org.jdom2.input.SAXBuilder

object RSSParser {

	case class RSSItem(title: String, link: String, pubDate: LocalDateTime, info: RSSInfo) {
		override def toString: String = s"{RSSItem: title=$title, link=$link, pubDate=$pubDate, info=$info"

		override def hashCode(): Int = new HashCodeBuilder(37, 17).append(title).append(info.hashCode()).hashCode()
	}

	case class RSSInfo(title: String, link: String, description: String) {
		override def toString: String = s"{RSSInfo: title=$title, link=$link, description=$description"

		override def hashCode(): Int = new HashCodeBuilder(37, 17).append(title).append(link).hashCode()
	}

	def get(url: URL): util.List[RSSItem] = {
		val sb = new SAXBuilder
		val doc = sb.build(url)
		val root = doc.getRootElement.getChild("channel")
		val resultItem = new util.ArrayList[RSSItem]()
		val items = root.getChildren("item").toArray()
		val info = RSSInfo(root.getChildTextNormalize("title"),
			root.getChildTextNormalize("link"),
			root.getChildTextNormalize("description"))
		for (item <- items) {
			val element = item.asInstanceOf[Element]
			resultItem add
				RSSItem(element.getChildTextNormalize("title"),
					element.getChildTextNormalize("link"),
					LocalDateTime.parse(element.getChildTextNormalize("pubDate"), DateTimeFormatter.RFC_1123_DATE_TIME),
					info)
		}
		resultItem
	}
}
