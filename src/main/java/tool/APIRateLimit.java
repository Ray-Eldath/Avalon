package tool;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APIRateLimit {
    private long timestamp = 0L;

    private long duration;

    public APIRateLimit(long duration) {
        this.duration = duration;
    }

    public synchronized boolean trySet(long currentInput) {
        long current = currentInput * 1000;
        if (current - timestamp > duration) {
            timestamp = current;
            return true;
        } else
            return false;
    }
}