package avalon.group

import avalon.api.Flag.AT
import avalon.extend.Recorder
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

	override fun permission(): ResponderPermission = ResponderPermission.ADMIN

	override fun getHelpMessage() = "avalon flush：<管理员> 刷新缓存并清除临时文件"

	override fun getKeyWordRegex(): Pattern = Pattern.compile("^avalon flush")

	override fun instance() = this
}