package avalon.api.util;

import avalon.util.Message;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

	@Override
	public int compareTo(Delayed o) {
		if (o == this)
			return 0;
		if (o == null || getClass() != o.getClass())
			return -1;
		DelayMessage message = (DelayMessage) o;
		long left = message.getDelaySecond();
		if (left == getDelaySecond()) return 0;
		return left > getDelaySecond() ? 1 : -1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DelayMessage that = (DelayMessage) o;
		return new EqualsBuilder()
				.append(delaySecondEpoch, that.delaySecondEpoch)
				.append(message, that.message)
				.append(doWhat, that.doWhat)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(delaySecondEpoch)
				.append(message)
				.append(doWhat)
				.toHashCode();
	}
}
