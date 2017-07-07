package avalon.tool;

import avalon.model.hook.FriendMessageHook;
import avalon.model.hook.GroupMessageHook;
import avalon.model.hook.HookType;
import avalon.util.FriendMessage;
import avalon.util.GroupMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class MessageHooker {
    private static Set<GroupMessageHook> groupMessageHooks = new HashSet<>();
    private static Set<FriendMessageHook> friendMessageHooks = new HashSet<>();

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
