package test;

import avalon.tool.ConfigSystem;

import static avalon.tool.ObjectCaster.toLongArray;

/**
 * Created by Eldath Ray on 2017/3/18 0018.
 *
 * @author Eldath Ray
 */
class ConfigSystemTest {
    public static void main(String[] args) {
        long[] test = toLongArray(ConfigSystem.getInstance().getConfigArray("Admin_Uid"));
        System.out.println(ConfigSystem.getInstance().getConfig("Block_Words"));

        for (Long long1 : test)
            System.out.println(long1);
    }
}
