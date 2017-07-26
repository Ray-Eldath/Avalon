package avalon.model.hook;

import avalon.util.GroupMessage;

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
     * @param hookType {@link HookType}
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMessageHook hook = (GroupMessageHook) o;
        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(consumer, hook.consumer)
                .append(hookType, hook.hookType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(13, 37)
                .append(consumer)
                .append(hookType)
                .toHashCode();
    }
}
