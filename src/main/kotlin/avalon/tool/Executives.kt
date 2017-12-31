package avalon.tool

import avalon.tool.system.Configs
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
	private val token = Configs.getResponderConfig("Execute", "token")

	private val suffixMap = mapOf(Pair("c", "c"), Pair("bash", "sh"))

	private fun parseLanguages(url: URL): Map<String, URL> {
		val arrayT = Share.get(url) ?: throw RuntimeException("no valid response")
		val array = arrayT.getJSONArray("array")
		val map = HashMap<String, URL>(array.length())
		(0 until array.length())
				.map(array::getJSONObject)
				.forEach { map.put(it.getString("name"), URL("${it.getString("url")}/latest")) }
		return map
	}

	override fun allLanguages(): List<String> = lang.keys.toList().sorted()

	override fun execute(language: String, codeLines: List<String>): ExecutiveResult {
		if (!lang.containsKey(language))
			throw UnsupportedLanguageException()
		val content = JSONObject()
		val array = JSONArray()

		var alpha = false
		for ((lang, suffix) in suffixMap) {
			if (language.equals(lang, true)) {
				content.put("name", "main.$suffix")
				alpha = true
			}
		}
		if (!alpha)
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
		val stdout = result.getString("stdout")
		val stderr = result.getString("stderr")
		val error = result.getString("error")

		val status = when {
			error.isNotEmpty() -> ExecutiveStatus.ERROR
			stderr.isNotEmpty() -> ExecutiveStatus.STDERR
			else -> ExecutiveStatus.OK
		}
		val exitCode = if (error.isNotEmpty() || stderr.isNotEmpty()) -1 else 0
		return ExecutiveResult(status, exitCode, stdout, stderr, error)
	}
}