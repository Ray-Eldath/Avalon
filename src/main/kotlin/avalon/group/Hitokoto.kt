package avalon.group

import avalon.tool.system.Configs
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import avalon.util.Service
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Pattern

object Hitokoto : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		message.response(Hitokotor.get())
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("(hitokoto|一言)", "获取一条一言。"),
					Pattern.compile("(hitokoto|一言)"),
					availableLocale = Locale.SIMPLIFIED_CHINESE
			)

	override fun instance() = this

	object Hitokotor : Service {
		private val category = Configs.getResponderConfig("Hitokoto", "category")
		private var url = "https://sslapi.hitokoto.cn/?encode=json"

		fun get(): String {
			if (category != null)
				url += "&c=$category"
			val obj = JSONTokener(URL(url).openStream().bufferedReader(StandardCharsets.UTF_8)).nextValue() as JSONObject
			return "『${obj.getString("hitokoto")}』\n—「${obj.getString("from")}」"
		}

		override fun available(): Boolean {
			return try {
				val obj = JSONTokener(URL(url).openStream().bufferedReader(StandardCharsets.UTF_8)).nextValue() as JSONObject
				obj.has("hitokoto")
			} catch (e: Exception) {
				false
			}
		}
	}
}