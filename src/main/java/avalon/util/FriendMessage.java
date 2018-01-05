package avalon.util;

import avalon.tool.pool.Constants;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;

import static avalon.tool.pool.Constants.Basic.CURRENT_SERVLET;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public class FriendMessage implements Message, Displayable {
	private final LocalDateTime time;
	private final long id, senderUid, timeLong;
	private final String senderNickName;
	private final String content;

	public FriendMessage(long id, LocalDateTime time, long senderUid, String senderNickName, String content) {
		this.id = id;
		this.time = time;
		this.timeLong = time.toEpochSecond(ZoneOffset.ofHours(Calendar.ZONE_OFFSET));
		this.senderUid = senderUid;
		this.senderNickName = senderNickName;
		this.content = content;
	}

	public FriendMessage(long id, long time, long senderUid, String senderNickName, String content) {
		this.id = id;
		this.time = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.of("Asia/Shanghai"));
		this.timeLong = time;
		this.senderUid = senderUid;
		this.senderNickName = senderNickName;
		this.content = content;
	}

	@Override
	public void response(String reply) {
		if (Constants.Basic.LOCAL_OUTPUT)
			System.out.println("Friend output: " + reply);
		else
			CURRENT_SERVLET.responseFriend(senderUid, reply);
	}

	@NotNull
	@Override
	public String getString() {
		return "id=" + id + "\ttime=" + time + "\tsenderUid=" + senderUid + "\tsenderNickName=\"" +
				senderNickName + "\"\tcontent=\"" + content + "\"";
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public LocalDateTime getTime() {
		return time;
	}

	@Override
	public long getTimeLong() {
		return timeLong;
	}

	@Override
	public long getSenderUid() {
		return senderUid;
	}

	@Override
	public String getSenderNickName() {
		return senderNickName;
	}

	@Override
	public String getContent() {
		return content;
	}
}
