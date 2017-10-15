package avalon.tool

import avalon.tool.system.Config
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class Executives {
	companion object {
		val EXECUTIVE = avalon.tool.GlotRun
	}
}

object GlotRun : Executive {
	override fun name(): String = "Glot-Run"

	private val lang: Map<String, URL> = parseLanguages(URL("https://run.glot.io/languages"))
	private val token = Config.getCommandConfig("Execute", "token")

	private fun parseLanguages(url: URL): Map<String, URL> {
		val arrayT = Share.get(url) ?: throw RuntimeException("no valid response")
		val array = arrayT.getJSONArray("array")
		val map = HashMap<String, URL>(array.length())
		(0 until array.length())
				.map(array::getJSONObject)
				.forEach {
					val name = it.getString("name")
					val urlH = URL(it.getString("url") + "/latest")
					map.put(name, urlH)
				}
		return map
	}

	override fun allLanguages(): List<String> = lang.keys.toList().sorted()

	override fun execute(language: String, codeLines: List<String>): ExecutiveResult {
		if (!lang.containsKey(language))
			throw UnsupportedLanguageException()
		val content = JSONObject()
		val array = JSONArray()
		content.put("name", "main")
		content.put("content", codeLines.joinToString("\n"))
		array.put(content)
		val obj = JSONObject()
		obj.put("files", array)
		val result = try {
			Share.post(lang[language]!!, obj,
					hashMapOf(
							Pair("Content-type", "application/json"),
							Pair("Authorization", "Token $token"))) ?: throw RuntimeException("nonnull `result`")
		} catch (exception: IOException) {
			JSONObject().apply {
				put("error", exception.toString())
				put("stdout", "")
				put("stderr", "")
			}
		}
		val stdout = result.getString("stdout").replace("\n", " ").trim()
		val stderr = result.getString("stderr").trim()
		val error = result.getString("error").trim()

		val status = when {
			error.isNotEmpty() -> ExecutiveStatus.ERROR
			stderr.isNotEmpty() -> ExecutiveStatus.STDERR
			else -> ExecutiveStatus.OK
		}
		val exitcode = if (error.isNotEmpty() || stderr.isNotEmpty()) -1 else 0
		return ExecutiveResult(status, exitcode, stdout, stderr, error)
	}
}