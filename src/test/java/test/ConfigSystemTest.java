package test;

import avalon.data.ConfigSystem;

/**
 * Created by Eldath Ray on 2017/3/18 0018.
 *
 * @author Eldath Ray
 */
class ConfigSystemTest {
    public static void main(String[] args) {
        long[] test = ConfigSystem.getInstance().getCommandAllowArray("CommandManager_basic");
        System.out.println(ConfigSystem.getInstance().getCommandConfig("AnswerMe", "BlockList_Words").getClass().toString());
        for (Long long1 : test)
            System.out.println(long1);
    }
}
