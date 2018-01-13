package avalon.tool

import avalon.tool.pool.Constants
import avalon.tool.system.Configs
import avalon.util.backend.CoolQBackend
import org.eclipse.jetty.util.UrlEncoded
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.net.URL
import java.nio.charset.Charset
import java.nio.file.Path
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

	/**
	 * @param groupUid 目的QQ群号
	 * @param message  消息文本，用`[Avalon:image]`表示图像
	 * @param image    图像文件
	 */
	@Deprecated("酷Q air不支持")
	fun respondGroupWithImage(groupUid: Long, message: String, image: Path) {
		if (Constants.Basic.CURRENT_SERVLET !is CoolQBackend)
			throw UnsupportedOperationException("only CooQServlet can handle image")
		val cq = "[CQ:image,file=file://$image]"
		Constants.Basic.CURRENT_SERVLET.responseGroup(groupUid, message.replace("[Avalon:image]", cq))
	}
}