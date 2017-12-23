package avalon.group

import avalon.tool.system.Configs
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

object Hitokoto : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		message.response(Hitokoto.get())
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("avalon (hitokoto|一言)", "获取一条一言。"),
					Pattern.compile("^avalon (hitokoto|一言)")
			)

	override fun instance() = this

	object Hitokoto {
		private val category = Configs.getResponderConfig("Hitokoto", "category")

		fun get(): String {
			var url = "https://sslapi.hitokoto.cn/?encode=json"
			if (category != null)
				url += "&c=$category"
			val obj = JSONTokener(URL(url).openStream().bufferedReader(StandardCharsets.UTF_8)).nextValue() as JSONObject
			return "『${obj.getString("hitokoto")}』\n—「${obj.getString("from")}」"
		}
	}
}