package avalon.util;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
public class GChatBlock extends GSection {
    private final Queue<GChat> chats = new ArrayDeque<>();

    public GChatBlock(GChat... input) {
        Collections.addAll(chats, input);
    }

    public boolean offer(GChat chat) {
        return chats.offer(chat);
    }

    public GChat poll() {
        return chats.poll();
    }

    @Override
    public String getString() {
        throw new UnsupportedOperationException("please use method \"poll\" instead.");
    }
}
