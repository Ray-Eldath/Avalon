package test;

import avalon.data.ConfigSystem;
import avalon.tool.ObjectCaster;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) {
        ObjectCaster.toLongArray(ConfigSystem.getInstance().getConfigArray("Admin_Uid"));
    }
}
