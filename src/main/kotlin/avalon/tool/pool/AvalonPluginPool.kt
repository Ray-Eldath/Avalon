package avalon.tool.pool

import avalon.tool.pool.Constants.Address.DATA_PATH
import avalon.util.Plugin
import avalon.util.PluginInfo
import org.json.JSONObject
import org.json.JSONTokener
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths

object AvalonPluginPool {
	private val setting = Paths.get("$DATA_PATH${File.separator}plugin${File.separator}plugins.json")
	private val infoList = arrayListOf<PluginInfo>()
	private val pluginList = arrayListOf<Plugin>()
	private val logger = LoggerFactory.getLogger(AvalonPluginPool.javaClass)

	private val main = (JSONTokener(Files.newBufferedReader(setting)).nextValue() as JSONObject).getJSONObject("plugins")

	init {
		val keySet = main.keySet()
		keySet.forEach {
			val o = main.getJSONObject(it)
			infoList += PluginInfo(
					it,
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
				logger.warn("plugin ${it.name} load failed: `${exception.localizedMessage}`")
			}
		}
	}

	private fun load(info: PluginInfo) {
		val plugin = URLClassLoader(
				Array(1) { URL("file:$DATA_PATH${File.separator}plugin${File.separator}${info.fileName}") },
				Thread.currentThread().contextClassLoader)
				.loadClass(info.classString) as Plugin
		pluginList.add(plugin)
		plugin.main()
	}

	fun getInfoList() = infoList

	fun getPluginList() = pluginList
}