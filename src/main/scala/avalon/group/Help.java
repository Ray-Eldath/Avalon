package avalon.group;

import avalon.api.CustomGroupResponder;
import avalon.api.RegisterResponder;
import avalon.api.util.Plugin;
import avalon.tool.pool.ConstantPool;
import avalon.util.GroupConfig;
import avalon.util.GroupMessage;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Help implements GroupMessageResponder {
	private static Help instance = null;

	public static Help getInstance() {
		if (instance == null) instance = new Help();
		return instance;
	}

	@Override
	public void doPost(GroupMessage message, GroupConfig groupConfig) {
		Map<Pattern, GroupMessageResponder> apiList = GroupMessageHandler.getApiList();
		Map<Pattern, CustomGroupResponder> customList = GroupMessageHandler.getCustomApiList();

		StringBuilder messageShow = new StringBuilder();
		for (GroupMessageResponder api : apiList.values()) {
			if (!GroupMessageHandler.getInstance().isResponderEnable(api))
				continue;
			String helpMessage = api.getHelpMessage();
			if (helpMessage == null || "".equals(helpMessage))
				continue;
			messageShow.append("\n").append(api.getHelpMessage());
		}
		if (!customList.isEmpty())
			for (CustomGroupResponder api : customList.values()) {
				Plugin plugin = RegisterResponder.queryAvalonPlugin(api);
				messageShow.append("以下指令由插件 ").append(plugin.name()).append(" 提供：");
				api.getHelpMessages().foreach(e -> messageShow.append("\t").append(e).append("\n"));
			}
		message.response("This is Avalon. 以下是我的帮助资料：\n" +
				"<关键词>：<触发的作用效果>，所有关键词均忽略大小写并且以avalon开头" + messageShow + "\nFor Avalon Version v" +
				ConstantPool.Version.avalon);
		// "\n（我才不会告诉你我有一些没有写在这里的彩蛋指令呢~哈哈`(*∩_∩*)′）");
	}

	@Override
	public String getHelpMessage() {
		return "avalon help|帮助：显示本内容";
	}

	@Override
	public Pattern getKeyWordRegex() {
		return Pattern.compile("avalon help|avalon 帮助");
	}

	@Override
	public GroupMessageResponder instance() {
		return null;
	}
}
