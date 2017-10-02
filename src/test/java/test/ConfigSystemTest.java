package test;

import avalon.tool.system.Config;

import static avalon.tool.ObjectCaster.toLongArray;

/**
 * Created by Eldath Ray on 2017/3/18 0018.
 *
 * @author Eldath Ray
 */
class ConfigSystemTest {
    public static void main(String[] args) {
	    long[] test = toLongArray(Config.instance().getConfigArray("Admin_Uid"));
	    System.out.println(Config.instance().get("Block_Words"));

        for (Long long1 : test)
            System.out.println(long1);
    }
}
