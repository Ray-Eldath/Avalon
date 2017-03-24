package tool;

import util.DelayMessage;
import util.Message;

import java.util.concurrent.DelayQueue;

/**
 * Created by Eldath Ray on 2017/3/22.
 *
 * @author Eldath Ray
 */
public class DelayResponse extends Thread {
    private final DelayQueue<DelayMessage> messages = new DelayQueue<>();

    public boolean delay(DelayMessage message) {
        return messages.offer(message);
    }

    @Override
    public void run() {
        DelayMessage delayMessage;
        Message message;
        while ((delayMessage = messages.poll()) != null) {
            message = delayMessage.getMessage();
            message.response(delayMessage.getDoWhat().get());
        }
    }
}
