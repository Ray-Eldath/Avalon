package util;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
class GChatBlock {
    private Queue<GChat> chats = new ArrayDeque<>();

    public GChatBlock(GChat... input) {
        Collections.addAll(chats, input);
    }

    public boolean addChat(GChat chat) {
        return chats.add(chat);
    }

    public GChat poll() {
        return chats.poll();
    }
}
