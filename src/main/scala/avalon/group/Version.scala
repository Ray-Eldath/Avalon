package avalon.group

import java.util.regex.Pattern

import avalon.tool.pool.ConstantPool.Basic.currentServlet
import avalon.tool.pool.{AvalonPluginPool, ConstantPool}
import avalon.util.{GroupConfig, GroupMessage}

/**
	* Created by Eldath on 2017/1/28 0028.
	*
	* @author Eldath
	*/
object Version extends GroupMessageResponder {
	override def doPost(message: GroupMessage, groupConfig: GroupConfig): Unit = {
		message.response(("Hi, I'm Avalon.\n我是阿瓦隆，QQ群机器人。\n我的名字和头像均取自《Implosion》，我由%s提供底层服务。" +
			"\n我由Ray Eldath进行二次开发。\n我在GitHub上开源，欢迎访问我的仓库：https://github.com/Ray-Eldath/Avalon" +
			"\n全部插件有：%s\n已装载的插件有：%s" +
			"\n%s Version: v%s\tMojo-Weixin Version: v%s\tAvalon Version: v%s")
			.format(currentServlet.name,
				AvalonPluginPool.getInfoList.map(_.getName),
				AvalonPluginPool.getInfoList.filter(_.isEnabled).map(_.getName),
				currentServlet.name,
				ConstantPool.Version.getInstance.servlet,
				ConstantPool.Version.getInstance.wechat,
				ConstantPool.Version.avalon))
	}

	override def getHelpMessage = "avalon version|版本：显示版本信息"

	override def getKeyWordRegex: Pattern = Pattern.compile("avalon version|avalon 版本|avalon about")

	override def instance: GroupMessageResponder = this
}