package avalon.extend

import avalon.tool.pool.Constants
import avalon.tool.system.Config
import avalon.tool.system.GroupConfig
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

object RSSFeeder : Runnable {
	private val urls = toURLList(Config.getResponderConfigArray("RSS", "feed"))
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
			GroupConfig.instance().followGroups.forEach { groupUid ->
				Constants.Basic.CURRENT_SERVLET.responseGroup(groupUid,
						"""订阅的RSS ${newest.info.title} - ${newest.info.description} 有更新：
${newest.title}
发布时间：${newest.pubDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))} 详见：${newest.link}""")
			}
		}
	}

	private fun toURLList(input: Array<Any>): List<URL> = input.filterIsInstance(String::class.java).map(::URL)
}