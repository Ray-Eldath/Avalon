package test;

import avalon.tool.MessageHooker;
import avalon.util.FriendMessageHook;
import avalon.util.GroupMessageHook;
import avalon.util.HookType;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) {
        MessageHooker.register(new GroupMessageHook(HookType.ALL, (e) -> {

        }));
        MessageHooker.register(new FriendMessageHook((e) -> {

        }));
    }
}
