package avalon.group

import avalon.tool.pool.AvalonPluginPool
import avalon.tool.pool.Constants
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.tool.pool.Constants.Basic.LANG
import avalon.tool.system.Configs
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.regex.Pattern

object Version : GroupMessageResponder() {
	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		val empty = LANG.getString("base.empty")
		message.response((LANG.getString("group.version.reply") + "\n" + LANG.getString("group.version.plugin") +
				"\n%s API Version: v%s\tAvalon Version: v%s%s")
				.format(CURRENT_SERVLET.name(),
						if (AvalonPluginPool.getInfoList().isEmpty())
							empty else AvalonPluginPool.getInfoList().map { it.name },
						if (AvalonPluginPool.getInfoList().isEmpty())
							empty else AvalonPluginPool.getInfoList().filter { it.isEnabled }.map { it.name },
						CURRENT_SERVLET.name(),
						Constants.Version.SERVLET,
						Constants.Version.AVALON,
						if (Configs.get("running_on_pc") as Boolean)
							("\n" + LANG.getString("group.version.running_on_pc")
									.format(CURRENT_SERVLET.getGroupSenderNickname(message.groupUid, groupConfig.owner),
											groupConfig.owner)) else ""))
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("(ver|about)", LANG.getString("group.version.help")),
					Pattern.compile("(ver|about)"),
					manageable = false
			)

	override fun instance() = this
}