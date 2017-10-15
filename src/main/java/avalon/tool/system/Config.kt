package avalon.tool.system

import org.json.*
import org.slf4j.LoggerFactory
import java.io.*

/**
 * Created by Eldath Ray on 2017/3/17.
 *
 * @author Eldath Ray
 * @since v0.0.1 Beta
 */
object Config : BaseConfigSystem {
	private lateinit var root: JSONObject
	private var allConfigs: Map<String, Any> = hashMapOf()
	private var pluginConfigs: Map<String, Any> = hashMapOf()
	private val logger = LoggerFactory.getLogger(Config::class.java)

	init {
		try {
			val path = File("").canonicalPath // 这里不用常量池是因为初始化的问题。反正别改。
			root = JSONTokener(FileReader(
					File(path + File.separator + "config.json"))).nextValue() as JSONObject
			allConfigs = jsonObjectToMap(root)
			pluginConfigs = jsonObjectToMap(root["plugin_config"] as JSONObject)
		} catch (e: IOException) {
			logger.error("Exception thrown while init Config: ", e)
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

	fun getCommandConfig(commandName: String, key: String): Any? {
		val obj = pluginConfigs[commandName] as JSONObject
		return if (obj.has(key)) obj[key] else null
	}

	fun isCommandEnable(name: String) = getCommandConfig(name, "enable") as Boolean

	fun getCommandConfigArray(commandName: String, key: String): Array<Any?> {
		val convert = (pluginConfigs[commandName] as JSONObject).getJSONArray(key)
		val result = arrayOfNulls<Any>(convert.length())
		for (i in 0 until convert.length())
			result[i] = convert[i]
		return result
	}

	object Companion {
		@JvmStatic
		fun instance(): Config = Config
	}
}
