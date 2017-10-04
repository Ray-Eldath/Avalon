package avalon.tool.pool

import avalon.api.util.Plugin
import avalon.api.util.PluginInfo
import avalon.tool.pool.Constants.Address.dataPath
import org.json.JSONObject
import org.json.JSONTokener
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths

object AvalonPluginPool {
	private val setting = Paths.get(dataPath + File.separator + "plugin" + File.separator + "plugins.json")
	private val infoList = ArrayList<PluginInfo>()
	private val pluginList = ArrayList<Plugin>()
	private val logger = LoggerFactory.getLogger(AvalonPluginPool.javaClass)

	private val main = (JSONTokener(Files.newBufferedReader(setting)).nextValue() as JSONObject).getJSONObject("plugins")

	init {
		val keySet = main.keySet()
		keySet.forEach {
			val o = main.getJSONObject(it)
			infoList += PluginInfo(
					it,
					o.getString("version"),
					o.getString("copyright"),
					o.getString("website"),
					o.getString("class"),
					o.getString("file"),
					o.getBoolean("enable")
			)
		}
	}

	fun load() {
		infoList.filter { it.isEnabled }.forEach {
			try {
				load(it)
			} catch (exception: Exception) {
				logger.warn("plugin ${it.name} load failed: $exception")
			}
		}
	}

	private fun load(info: PluginInfo) {
		val plugin = URLClassLoader(Array(1) { URL("file:" + dataPath + File.separator + "plugin" + File.separator + info.fileName) },
				Thread.currentThread().contextClassLoader).loadClass(info.classString).newInstance() as Plugin
		pluginList.add(plugin)
		plugin.main()
	}

	fun getInfoList(): List<PluginInfo> = infoList

	fun getPluginList(): List<Plugin> = pluginList
}