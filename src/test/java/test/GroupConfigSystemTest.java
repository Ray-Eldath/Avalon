package test;

import avalon.tool.system.GroupConfig;
import org.apache.commons.lang3.ArrayUtils;

public class GroupConfigSystemTest {
	public static void main(String[] args) {
		avalon.util.GroupConfig config = GroupConfig.instance().getConfig(399863405);
		System.out.println(ArrayUtils.toString(config.getAdmin()));
		System.out.println(ArrayUtils.toString(config.getBlacklist()));
		config.getPermissions().forEach(System.out::println);
		System.out.println(config.isListen());
		System.out.println(config.isRecord());
	}
}
