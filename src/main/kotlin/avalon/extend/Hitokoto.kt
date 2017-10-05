package avalon.extend

import avalon.tool.system.Config
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

object Hitokoto {
	private val category = Config.instance().getCommandConfig("Hitokoto", "category")

	fun get(): String {
		var url = "https://sslapi.hitokoto.cn/?encode=json"
		if (category != null)
			url += "&c=$category"

		val obj = JSONTokener(URL(url).openStream()).nextValue() as JSONObject
		val hitokoto = obj.getString("hitokoto")
		val from = obj.getString("from")

		return "$hitokoto\n——$from"
	}
}