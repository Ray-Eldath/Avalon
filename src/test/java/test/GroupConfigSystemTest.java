package test;

import avalon.tool.system.GroupConfigSystem;
import avalon.util.GroupConfig;
import org.apache.commons.lang3.ArrayUtils;

public class GroupConfigSystemTest {
	public static void main(String[] args) {
		GroupConfig config = GroupConfigSystem.instance().getConfig(399863405);
		System.out.println(ArrayUtils.toString(config.getAdmin()));
		System.out.println(ArrayUtils.toString(config.getBlacklist()));
		config.getPermissions().forEach(System.out::println);
		System.out.println(config.isListen());
		System.out.println(config.isRecord());
	}
}
