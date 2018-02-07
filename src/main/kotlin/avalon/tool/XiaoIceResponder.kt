package avalon.tool

import avalon.tool.system.Configs
import org.eclipse.jetty.util.UrlEncoded
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.net.URL
import java.nio.charset.Charset
import java.util.logging.Logger

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
object XiaoIceResponder {
	private val logger = Logger.getGlobal()
	private val replaceList = arrayListOf("小怪冰", "小冰冰", "冰酱", "小冰")

	fun responseXiaoIce(content: String): String? {
		try {
			val obj = JSONTokener(URL(
					"${Configs.getResponderConfig("AnswerMe", "mojo-weixin_api_address").toString()}/openwx/consult?account=xiaoice-ms&content=${UrlEncoded.encodeString(content)}")
					.openStream()).nextValue() as JSONObject
			if (obj.isNull("reply")) return null
			var reply = String(obj["reply"].toString().toByteArray(), Charset.forName("UTF-8"))
			if ("[语音]" in reply) return null
			else if ("[图片]" in reply) return null
			for (thisReplaceWord in replaceList)
				reply = reply.replace(thisReplaceWord, "Avalon")
			return reply
		} catch (e: IOException) {
			logger.warning("IOException thrown while responseXiaoIce: `${e.localizedMessage}`")
			return null
		}

	}
}