package avalon.plugin

import avalon.tool.pool.Constants
import avalon.tool.system.Configs
import avalon.tool.system.GroupConfigs
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.jdom2.input.SAXBuilder
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

object RSSFeeder : Runnable {
	private val urls = toURLList(Configs.getResponderConfigArray("RSS", "feed"))
	private val updated = hashMapOf(*urls.map { it to LocalDateTime.MIN }.toTypedArray())
	private val random = Random()

	private val allItemsMap: Map<URL, List<RSSParser.RSSItem>>
		get() = mapOf(*urls.map { it to RSSParser[it] }.toTypedArray())

	private fun update(): RSSParser.RSSItem? {
		val map = allItemsMap
		val result = ArrayList<RSSParser.RSSItem>()
		for ((key, value) in map) {
			for (thisItem in value)
				if (updated.containsKey(key) && !value.isEmpty() && thisItem.pubDate.isAfter(updated[key])) {
					result.add(thisItem)
					updated.replace(key, thisItem.pubDate)
				}
		}
		return if (result.isEmpty()) null else result[random.nextInt(result.size)]
	}

	override fun run() {
		update()?.let { newest ->
			GroupConfigs.instance().followGroups.forEach { groupUid ->
				Constants.Basic.CURRENT_SERVLET.responseGroup(groupUid,
						"""订阅的RSS ${newest.info.title} - ${newest.info.description} 有更新：
${newest.title}
发布时间：${newest.pubDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))} 详见：${newest.link}""")
			}
		}
	}

	private fun toURLList(input: Array<Any>): List<URL> = input.filterIsInstance(String::class.java).map(::URL)
}

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
			if (this === other || other === null) return true
			if (javaClass != other.javaClass) return false
			other as RSSItem
			return EqualsBuilder().append(other.title, title).append(other.link, link).isEquals
		}
	}

	class RSSInfo(val title: String, val link: String, val description: String) {
		override fun toString(): String = "{RSSInfo: title=$title, link=$link, description=$description"

		override fun hashCode(): Int = HashCodeBuilder(37, 17).append(title).append(link).hashCode()
		override fun equals(other: Any?): Boolean {
			if (this === other || other === null) return true
			if (javaClass != other.javaClass) return false
			other as RSSInfo
			return EqualsBuilder().append(other.title, title).append(other.link, link).isEquals
		}
	}

	@JvmStatic
	operator fun get(url: URL): List<RSSItem> {
		val sb = SAXBuilder()
		val doc = sb.build(url.openConnection().getInputStream())
		val root = doc.rootElement.getChild("channel")
		val items = root.getChildren("item").toList()
		val info = RSSInfo(root.getChildTextNormalize("title"),
				root.getChildTextNormalize("link"),
				root.getChildTextNormalize("description"))
		return items.map {
			RSSItem(it.getChildTextNormalize("title"),
					it.getChildTextNormalize("link"),
					LocalDateTime.parse(it.getChildTextNormalize("pubDate"), DateTimeFormatter.RFC_1123_DATE_TIME),
					info)
		}
	}

}