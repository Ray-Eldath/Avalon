package avalon.api

import avalon.tool.pool.Constants.Basic.currentServlet
import avalon.util.GroupMessage
import avalon.util.servlet.CoolQServlet

object Flag {
	def AT(message: GroupMessage): String =
		if (currentServlet.isInstanceOf[CoolQServlet])
			"[CQ:at,qq=" + message.getSenderUid + "]"
		else
			"@" + message.getSenderNickName

	def AT(userUid: Long): String =
		if (currentServlet.isInstanceOf[CoolQServlet])
			"[CQ:at,qq=" + userUid + "]"
		else
			"@" + userUid
}