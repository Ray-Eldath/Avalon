package avalon.extend

import avalon.tool.system.Config
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import java.nio.charset.StandardCharsets

object Hitokoto {
	private val category = Config.getResponderConfig("Hitokoto", "category")

	fun get(): String {
		var url = "https://sslapi.hitokoto.cn/?encode=json"
		if (category != null)
			url += "&c=$category"
		val obj = JSONTokener(URL(url).openStream().bufferedReader(StandardCharsets.UTF_8)).nextValue() as JSONObject
		return "『${obj.getString("hitokoto")}』\n—「${obj.getString("from")}」"
	}
}