package avalon.tool;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author 磷
 */
public class APIRateLimit {
	private long timestamp = 0L;

	private final long duration;

	public APIRateLimit(long duration) {
		this.duration = duration;
	}

	public synchronized boolean trySet(long current) {
		if (current - timestamp > duration) {
			timestamp = current;
			return true;
		} else
			return false;
	}
}