package avalon.tool

import org.junit.Test

class ExecutiveTest {
	@Test
	fun GlotRunTest() {
		println(GlotRun.execute("python", arrayListOf("print(\"alpha\")")))
	}
}