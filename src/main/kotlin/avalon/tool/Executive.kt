package avalon.tool

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

enum class ExecutiveStatus {
	OK, ERROR, STDERR
}

class ExecutiveResult(val status: ExecutiveStatus, val exitcode: Int, val stdout: String, val stderr: String, val error: String) {
	override fun toString() = "ExecutiveResult(buildStatus=$status, exitcode=$exitcode, stdout='$stdout', stderr='$stderr', error='$error')"
}

interface Executive {
	fun name(): String
	fun allLanguages(): List<String>
	fun execute(language: String, codeLines: List<String>): ExecutiveResult
}

class UnsupportedLanguageException : RuntimeException()

object Share {

	fun post(url: URL, content: JSONObject? = null, parameter: Map<String, String>? = null) =
			visit("POST", url, content, parameter)

	fun get(url: URL, content: JSONObject? = null, parameter: Map<String, String>? = null) =
			visit("GET", url, content, parameter)

	private fun visit(method: String, url: URL, content: JSONObject? = null,
	                  parameter: Map<String, String>? = null): JSONObject? {
		val connection = url.openConnection() as HttpURLConnection
		if (parameter != null)
			for (entry in parameter.entries)
				connection.setRequestProperty(entry.key, entry.value)
		connection.connectTimeout = 6000
		connection.doInput = true
		connection.doOutput = true
		connection.requestMethod = method

		content?.let {
			val stream = connection.outputStream
			val writer = OutputStreamWriter(stream, StandardCharsets.UTF_8)
			it.write(writer)
			writer.close()
			stream.close()
		}

		return if (connection.responseCode >= HttpURLConnection.HTTP_BAD_REQUEST)
			readStream(connection.errorStream)!!.apply {
				put("internal_error", true)
			}
		else readStream(connection.inputStream)!!.apply {
			put("internal_error", false)
		}
	}

	private fun readStream(input: InputStream): JSONObject? {
		val tmp = input.read()
		return if (tmp == -1) {
			input.close()
			null
		} else {
			val tokener = JSONTokener(tmp.toChar() + InputStreamReader(input).readText())
			var obj = JSONObject()
			if (tmp.toChar() == '[') obj.put("array", JSONArray(tokener)) else
				obj = JSONObject(tokener)
			input.close()
			obj
		}
	}
}