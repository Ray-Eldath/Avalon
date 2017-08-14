package avalon.tool.pool

import java.io.File
import java.net.{MalformedURLException, URL, URLClassLoader}
import java.nio.file.{Files, Paths}

import avalon.tool.pool.ConstantPool.Address.dataPath
import avalon.util.{Plugin, PluginInfo}
import org.json.{JSONObject, JSONTokener}
import org.slf4j.LoggerFactory.getLogger

import scala.collection.mutable.ArrayBuffer

/**
	* Created by Eldath Ray on 2017/5/27 0027.
	*
	* @author Eldath Ray
	*/
object AvalonPluginPool {
	private val setting = Paths.get(dataPath + File.separator + "plugin" + File.separator + "plugins.json")
	private val infoList = new ArrayBuffer[PluginInfo]
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

	@throws[MalformedURLException]
	@throws[ClassNotFoundException]
	@throws[IllegalAccessException]
	@throws[InstantiationException]
	private def load(info: PluginInfo): Unit = {
		val plugin = new URLClassLoader(Array[URL]
			(new URL("file:" + dataPath + File.separator + "plugin" + File.separator + info.getFileName)),
			Thread.currentThread.getContextClassLoader).loadClass(info.getClassString).newInstance.asInstanceOf[Plugin]
		plugin.main()
	}

	def getInfoList: ArrayBuffer[PluginInfo] = infoList.result()
}