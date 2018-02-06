package avalon.util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.time.LocalDateTime;

/**
 * Created by Eldath on 2017/2/12 0012.
 *
 * @author Eldath
 */
public abstract class Message implements Displayable {
	public abstract String getContent();

	public abstract LocalDateTime getTime();

	public abstract long getTimeLong();

	public abstract long getId();

	public abstract long getSenderUid();

	public abstract String getSenderNickName();

	public abstract void response(String reply);

	@Override
	@NotNull
	public abstract String getString();

	private final JSONObject extras = new JSONObject();

	public void setExtra(String key, Object value) {
		this.extras.put(key, value);
	}

	public JSONObject getExtras() {
		return extras;
	}
}
