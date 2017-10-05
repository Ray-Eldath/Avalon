package avalon.group

import avalon.tool.pool.AvalonPluginPool
import avalon.tool.pool.Constants
import avalon.tool.pool.Constants.Basic.currentServlet
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Version : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		message.response(("Hi, I'm Avalon.\n我是阿瓦隆，QQ群机器人。\n我的名字和头像均取自《Implosion》，我由%s提供底层服务。" +
				"\n我由Ray Eldath进行二次开发。\n我在GitHub上开源，欢迎访问我的仓库：https://github.com/Ray-Eldath/Avalon" +
				"\n全部插件有：%s\n已装载的插件有：%s" +
				"\n%s Version: v%s\tAvalon Version: v%s")
				.format(currentServlet.name(),
						if (AvalonPluginPool.getInfoList().isEmpty())
							"<无>" else AvalonPluginPool.getInfoList().map { it.name },
						if (AvalonPluginPool.getInfoList().isEmpty())
							"<无>" else AvalonPluginPool.getInfoList().filter { it.isEnabled }.map { it.name },
						currentServlet.name(),
						Constants.Version.instance().servlet(),
						Constants.Version.avalon))
	}

	override fun getHelpMessage() = "avalon (version|about|版本)：显示版本信息"

	override fun getKeyWordRegex(): Pattern = Pattern.compile("^avalon (version|about|版本)")

	override fun instance(): GroupMessageResponder = this
}