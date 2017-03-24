package util;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by Eldath Ray on 2017/3/22.
 *
 * @author Eldath Ray
 */
public class DelayMessage implements Delayed {
    private final long delaySecond;
    private final long delaySecondEpoch;
    private final Message message;
    private final Supplier<String> doWhat;

    public DelayMessage(long delaySecond, Message message, Supplier<String> doWhat) {
        this.delaySecond = delaySecond;
        this.delaySecondEpoch = System.currentTimeMillis() + delaySecond;
        this.message = message;
        this.doWhat = doWhat;
    }

    public Message getMessage() {
        return message;
    }

    public Supplier<String> getDoWhat() {
        return doWhat;
    }

    private long getDelaySecond() {
        return delaySecond;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delaySecondEpoch - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    //FIXME 需要审查
    @Override
    public int compareTo(Delayed o) {
        if (o == this)
            return 0;
        if (o == null || !(o instanceof Message)) return -1;
        DelayMessage message = (DelayMessage) o;
        long left = message.getDelaySecond();
        if (left == getDelaySecond()) return 0;
        return left > getDelaySecond() ? 1 : -1;
    }
}
