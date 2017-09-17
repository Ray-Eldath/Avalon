package avalon.tool.pool

import java.io.File
import java.net.{URL, URLClassLoader}
import java.nio.file.{Files, Paths}
import java.util

import avalon.api.util.{Plugin, PluginInfo}
import avalon.tool.pool.ConstantPool.Address.dataPath
import org.json.{JSONObject, JSONTokener}
import org.slf4j.LoggerFactory.getLogger

import scala.collection.mutable.ListBuffer

/**
	* Created by Eldath Ray on 2017/5/27 0027.
	*
	* @author Eldath Ray
	*/
object AvalonPluginPool {
	private val setting = Paths.get(dataPath + File.separator + "plugin" + File.separator + "plugins.json")
	private val infoList = new ListBuffer[PluginInfo]
	private val pluginList = new util.ArrayList[Plugin]
	private val logger = getLogger(AvalonPluginPool.getClass)

	private val main = new JSONTokener(Files.newBufferedReader(setting)).nextValue.asInstanceOf[JSONObject].getJSONObject("plugins")

	private val keySet = main.keySet
	keySet.forEach((e: String) => {
		val o = main.getJSONObject(e)
		infoList += new PluginInfo(
			e,
			o.getString("version"),
			o.getString("copyright"),
			o.getString("website"),
			o.getString("class"),
			o.getString("file"),
			o.getBoolean("enable"))
	})

	def load(): Unit = {
		infoList filter (_.isEnabled) foreach ((e: PluginInfo) => {
			try load(e)
			catch {
				case e1: Exception =>
					logger.warn("plugin " + e.getName + " load failed: " + e1.toString)
			}
		})
	}

	private def load(info: PluginInfo): Unit = {
		val plugin = new URLClassLoader(Array[URL]
			(new URL("file:" + dataPath + File.separator + "plugin" + File.separator + info.getFileName)),
			Thread.currentThread.getContextClassLoader).loadClass(info.getClassString).newInstance.asInstanceOf[Plugin]
		pluginList.add(plugin)
		plugin.main()
	}

	def getInfoList: List[PluginInfo] = infoList.result()

	def getPluginList: util.ArrayList[Plugin] = pluginList
}