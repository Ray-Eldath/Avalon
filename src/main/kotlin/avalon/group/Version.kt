package avalon.group

import avalon.tool.pool.AvalonPluginPool
import avalon.tool.pool.Constants
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.tool.system.Configs
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Version : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		message.response(("Hi, I'm Avalon.\n我是阿瓦隆，QQ群机器人。\n我的名字和头像均取自《Implosion》，我由%s提供底层服务。" +
				"\n我由Ray Eldath开发。\n我在GitHub上开源，欢迎访问我的仓库：https://github.com/Ray-Eldath/Avalon" +
				"\n全部插件有：%s\n已装载的插件有：%s" +
				"\n%s Version: v%s\tAvalon Version: v%s%s")
				.format(CURRENT_SERVLET.name(),
						if (AvalonPluginPool.getInfoList().isEmpty())
							"<无>" else AvalonPluginPool.getInfoList().map { it.name },
						if (AvalonPluginPool.getInfoList().isEmpty())
							"<无>" else AvalonPluginPool.getInfoList().filter { it.isEnabled }.map { it.name },
						CURRENT_SERVLET.name(),
						Constants.Version.instance().servlet(),
						Constants.Version.AVALON,
						if (Configs.get("running_on_pc") as Boolean)
							"\n我运行在个人计算机（而不是服务器）上，故无法提供稳定服务。若有其它疑问请联系我的主人`${CURRENT_SERVLET.getGroupSenderNickname(message.groupUid, groupConfig.owner)} - ${groupConfig.owner}`" else ""))
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("avalon (version|about|版本)", "显示及相关信息"),
					Pattern.compile("^avalon (version|about|版本)"),
					manageable = false
			)

	override fun instance() = this
}