package avalon.util;

import java.util.function.Consumer;

/**
 * Created by Eldath Ray on 2017/4/17 0017.
 *
 * @author Eldath Ray
 */
public class GroupMessageHook {
    private Consumer<GroupMessage> consumer;
    private HookType hookType;

    /**
     * @param hookType {@link avalon.api.util.HookType}
     * @param consumer When message received and your hook is meet the conditions (HookType),
     *                 the consumer will be run.
     */
    public GroupMessageHook(HookType hookType, Consumer<GroupMessage> consumer) {
        this.hookType = hookType;
        this.consumer = consumer;
    }

    public HookType getHookType() {
        return hookType;
    }

    public Consumer<GroupMessage> getConsumer() {
        return consumer;
    }
}
