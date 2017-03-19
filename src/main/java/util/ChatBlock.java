package util;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
class ChatBlock {
    private Queue<Chat> chats = new ArrayDeque<>();

    public ChatBlock(Chat... input) {
        Collections.addAll(chats, input);
    }

    public boolean addChat(Chat chat) {
        return chats.add(chat);
    }

    public Chat poll() {
        return chats.poll();
    }
}
