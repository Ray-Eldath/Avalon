package avalon.tool;

import avalon.util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class MessageHooker {
    private static List<GroupMessageHook> groupMessageHooks = new ArrayList<>();
    private static List<FriendMessageHook> friendMessageHooks = new ArrayList<>();

    public static void handle(boolean isAdmin, GroupMessage message) {
        groupMessageHooks.forEach(e -> {
            HookType type = e.getHookType();
            if (type == HookType.ALL)
                e.getConsumer().accept(message);
            else if (type == HookType.ADMIN_ONLY && isAdmin)
                e.getConsumer().accept(message);
            else if (type == HookType.GENERAL_USER_ONLY && !isAdmin)
                e.getConsumer().accept(message);
        });
    }

    public static void handle(FriendMessage message) {
        friendMessageHooks.forEach(e -> e.getConsumer().accept(message));
    }

    public static boolean register(GroupMessageHook hook) {
        return groupMessageHooks.add(hook);
    }

    public static boolean register(FriendMessageHook hook) {
        return friendMessageHooks.add(hook);
    }
}
