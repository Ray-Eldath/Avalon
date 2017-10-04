package avalon.group

import java.util.regex.Pattern

import avalon.tool.pool.Constants
import avalon.util.{GroupConfig, GroupMessage}
import org.slf4j.LoggerFactory

/**
	* Created by Eldath Ray on 2017/3/25 0025.
	*
	* @author Eldath Ray
	*/
object Shutdown extends GroupMessageResponder {
	override def doPost(message: GroupMessage, groupConfig: GroupConfig): Unit = {
		val admins = groupConfig.getAdmin
		for (admin <- admins) {
			if (admin == message.getSenderUid) {
				try Constants.Basic.currentServlet.shutdown()
				catch {
					case e: UnsupportedOperationException =>
						message.response(e.getMessage)
				}
				LoggerFactory.getLogger(Shutdown.getClass).warn("Avalon is stopped remotely by " +
					message.getSenderUid + " : " + message.getSenderNickName + " on " + message.getGroupUid + " : " +
					message.getGroupName + " at " + message.getTime.toString.replace("T", " "))
				System.exit(0)
			}
		}
	}

	override def getHelpMessage = "avalon (shutdown|exit)：<管理员> 退出Avalon。"

	override def getKeyWordRegex: Pattern = Pattern.compile("^avalon (shutdown|exit)")

	override def instance: GroupMessageResponder = this
}