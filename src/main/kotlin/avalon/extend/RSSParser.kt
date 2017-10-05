package avalon.extend

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.jdom2.Element
import org.jdom2.input.SAXBuilder
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Eldath Ray on 2017/10/5 0021.
 *
 * @author Eldath Ray
 */
object RSSParser {
	class RSSItem(val title: String, val link: String, val pubDate: LocalDateTime, val info: RSSInfo) {
		override fun toString(): String = "{RSSItem: title=$title, link=$link, pubDate=$pubDate, info=$info"

		override fun hashCode(): Int = HashCodeBuilder(37, 17).append(title).append(info.hashCode()).hashCode()
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			other as RSSItem
			return EqualsBuilder().append(other.title, title).append(other.link, link).isEquals
		}
	}

	class RSSInfo(val title: String, val link: String, val description: String) {
		override fun toString(): String = "{RSSInfo: title=$title, link=$link, description=$description"

		override fun hashCode(): Int = HashCodeBuilder(37, 17).append(title).append(link).hashCode()
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			other as RSSInfo
			return EqualsBuilder().append(other.title, title).append(other.link, link).isEquals
		}
	}

	@JvmStatic
	fun get(url: URL): List<RSSItem> {
		val sb = SAXBuilder()
		val doc = sb.build(url)
		val root = doc.rootElement.getChild("channel")
		val items = root.getChildren("item").toList()
		val info = RSSInfo(root.getChildTextNormalize("title"),
				root.getChildTextNormalize("link"),
				root.getChildTextNormalize("description"))
		return items.map { it as Element }
				.map {
					RSSItem(it.getChildTextNormalize("title"),
							it.getChildTextNormalize("link"),
							LocalDateTime.parse(it.getChildTextNormalize("pubDate"), DateTimeFormatter.RFC_1123_DATE_TIME),
							info)
				}
	}

}