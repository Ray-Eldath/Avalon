package avalon.tool

import org.junit.Test

class ExecutiveTest {
	@Test
	fun tGlotRunTest() {
		println(GlotRun.execute("bash", arrayListOf("lss /bin")))
	}
}