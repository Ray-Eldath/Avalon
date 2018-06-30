package avalon.group

import avalon.api.Flag.at
import avalon.function.Recorder
import avalon.tool.pool.Constants.Basic.LANG
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

/**
 * Created by Eldath Ray on 2017/10/5 0026.
 *
 * @author Eldath Ray
 */
object Flush : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		Recorder.getInstance().flushNow()
		System.gc()
		message.response("${at(message)} ${LANG.getString("group.flush.reply")}")
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("flush", LANG.getString("group.flush.help")),
					Pattern.compile("flush"),
					permission = ResponderPermission.ADMIN,
					manageable = false
			)

	override fun instance() = this
}