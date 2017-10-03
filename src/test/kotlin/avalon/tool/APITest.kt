package avalon.tool

import avalon.tool.pool.Constants
import org.junit.Test

class APITest {
	@Test
	fun groupNicknameTest() {
		println(Constants.Basic.currentServlet.getGroupSenderNickname(319293196, 14644412123139))
	}
}