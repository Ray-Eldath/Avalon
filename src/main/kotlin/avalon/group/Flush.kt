package avalon.group

import avalon.api.Flag.AT
import avalon.function.Recorder
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
		message.response("管理员：${AT(message)} 缓存及临时文件刷新成功。")
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("avalon flush", "刷新缓存并清除临时文件"),
					Pattern.compile("^avalon flush"),
					permission = ResponderPermission.ADMIN,
					manageable = false
			)

	override fun instance() = this
}