package avalon.util;

import java.util.function.Consumer;

/**
 * Created by Eldath Ray on 2017/4/17 0017.
 *
 * @author Eldath Ray
 */
public class FriendMessageHook {
    private Consumer<FriendMessage> consumer;

    /**
     * @param consumer When message received, the consumer will be run.
     */
    public FriendMessageHook(Consumer<FriendMessage> consumer) {
        this.consumer = consumer;
    }

    public Consumer<FriendMessage> getConsumer() {
        return consumer;
    }
}
