package avalon.tool.system

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader
import java.io.IOException

/**
 * Created by Eldath Ray on 2017/3/17.
 *
 * @author Eldath Ray
 * @since v0.0.1 Beta
 */
object Configs : BaseConfigSystem {
	private lateinit var root: JSONObject
	private var allConfigs: Map<String, Any> = hashMapOf()
	private var responderConfigs: Map<String, Any> = hashMapOf()
	private var functionConfigs: Map<String, Any> = hashMapOf()
	private val logger = LoggerFactory.getLogger(Configs::class.java)

	init {
		try {
			val path = File("").canonicalPath // 这里不用常量池是因为初始化的问题。反正别改。
			root = JSONTokener(FileReader(
					File(path + File.separator + "config.json"))).nextValue() as JSONObject
			allConfigs = jsonObjectToMap(root)
			responderConfigs = jsonObjectToMap(root["responder_config"] as JSONObject)
			functionConfigs = jsonObjectToMap(root["function_config"] as JSONObject)
		} catch (e: IOException) {
			logger.error("Exception thrown while init Configs: ", e)
		}

	}

	private fun jsonObjectToMap(obj: JSONObject): Map<String, Any> {
		val result = hashMapOf<String, Any>()
		val names = obj.names()
		for (i in 0 until obj.length()) {
			val key = names[i].toString()
			if ("comment" in key)
				continue
			val thisObject = obj[key]
			result.put(key, thisObject)
		}
		return result
	}

	override operator fun get(key: String): Any? = allConfigs[key]

	override fun getString(key: String): String =
			allConfigs[key] as? String ?: throw UnsupportedOperationException("value invalid: not a String")

	fun getJSONObject(key: String): JSONObject = root.getJSONObject(key)

	fun getConfigArray(key: String): Array<Any?> {
		val array = allConfigs[key] as JSONArray
		val result = arrayOfNulls<Any>(array.length())
		for (i in 0 until array.length()) result[i] = array[i]
		return result
	}

	fun getResponderConfig(responderName: String, key: String): Any? =
			getObjectConfig(responderConfigs, responderName, key)

	fun getResponderConfigArray(responderName: String, key: String): Array<Any> =
			getObjectConfigArray(responderConfigs, responderName, key)

	fun isPluginEnable(pluginName: String): Boolean =
			getPluginConfig(pluginName, "enable") as Boolean

	fun getPluginConfig(pluginName: String, key: String): Any? =
			getObjectConfig(functionConfigs, pluginName, key)

	fun getPluginConfigArray(pluginName: String, key: String): Array<Any> =
			getObjectConfigArray(functionConfigs, pluginName, key)

	private fun getObjectConfig(`object`: Map<String, Any>, key1: String, key2: String): Any? {
		val obj = `object`[key1] as JSONObject
		return if (obj.has(key2)) obj[key2] else null
	}

	private fun getObjectConfigArray(`object`: Map<String, Any>, key1: String, key2: String): Array<Any> {
		return (`object`[key1] as JSONObject).getJSONArray(key2).map { it }.toTypedArray()
	}

	object Companion {
		@JvmStatic
		fun instance(): Configs = Configs
	}
}