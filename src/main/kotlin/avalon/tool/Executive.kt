package avalon.tool

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

enum class ExecutiveStatus {
	OK, ERROR, STDERR
}

class ExecutiveResult(val status: ExecutiveStatus, val exitcode: Int, val stdout: String, val stderr: String, val error: String) {
	override fun toString(): String {
		return "ExecutiveResult(status=$status, exitcode=$exitcode, stdout='$stdout', stderr='$stderr', error='$error')"
	}
}

interface Executive {
	fun allLanguages(): List<String>
	fun execute(language: String, codeLines: List<String>): ExecutiveResult
}

class UnsupportedLanguageException : Exception()

internal object Share {

	fun post(url: URL, content: JSONObject? = null, parameter: Map<String, String>? = null): JSONObject? =
			visit("POST", url, content, parameter)

	fun get(url: URL, content: JSONObject? = null, parameter: Map<String, String>? = null): JSONObject? =
			visit("GET", url, content, parameter)

	private fun visit(method: String, url: URL, content: JSONObject? = null,
	                  parameter: Map<String, String>? = null): JSONObject? {
		val connection = url.openConnection() as HttpURLConnection
		if (parameter != null)
			for (entry in parameter.entries)
				connection.setRequestProperty(entry.key, entry.value)
		connection.doInput = true
		connection.doOutput = true
		connection.requestMethod = method

		if (content != null) {
			val stream = connection.outputStream
			val writer = OutputStreamWriter(stream)
			content.write(writer)
			writer.close()
			stream.close()
		}
		val input = connection.inputStream

		val tmp = input.read()
		return if (tmp == -1) {
			input.close()
			null
		} else {
			val tokener = JSONTokener(tmp.toChar() + InputStreamReader(input).readText())
			var obj = JSONObject()
			if (tmp.toChar() == '[') {
				obj.put("array", JSONArray(tokener))
			} else
				obj = JSONObject(tokener)
			input.close()
			obj
		}
	}
}