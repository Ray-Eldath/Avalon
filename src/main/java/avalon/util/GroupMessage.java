package avalon.util;

import avalon.group.GroupMessageHandler;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
public class GroupMessage implements Message, Displayable {
	private final LocalDateTime time;
	private final long id, senderUid, groupUid, timeLong;
	private final String groupName, content, senderNickName;

	public GroupMessage(long id, LocalDateTime time, long senderUid, String senderNickName, long groupUid,
	                    String groupName, String content) {
		this.id = id;
		this.time = time;
		this.timeLong = time.toEpochSecond(ZoneOffset.ofHours(Calendar.ZONE_OFFSET));
		this.senderUid = senderUid;
		this.groupUid = groupUid;
		this.senderNickName = senderNickName;
		this.groupName = groupName;
		this.content = content;
	}

	public GroupMessage(long id, long time, long senderUid, String senderNickName, long groupUid,
	                    String groupName, String content) {
		this.id = id;
		this.timeLong = time;
		this.time = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.of("Asia/Shanghai"));
		this.senderUid = senderUid;
		this.groupUid = groupUid;
		this.senderNickName = senderNickName;
		this.groupName = groupName;
		this.content = content;
	}

	@Override
	public void response(String reply) {
		String finalReply = reply;
		for (String thisBlockWord : GroupMessageHandler.INSTANCE.getBlockWordList())
			if (reply.toLowerCase().replaceAll("[\\pP\\p{Punct}]", "").contains(thisBlockWord))
				finalReply = "Avalon不会发送含屏蔽词的消息。";
		CURRENT_SERVLET.responseGroup(groupUid, finalReply);
	}

	public void response(String reply, int shutUpTime) {
		response(reply);
		CURRENT_SERVLET.shutUp(groupUid, senderUid, shutUpTime);
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


	public long getGroupUid() {
		return groupUid;
	}

	@Override
	public String getSenderNickName() {
		return senderNickName;
	}

	public String getGroupName() {
		return groupName;
	}

	@Override
	public String getContent() {
		return content;
	}

	@NotNull
	@Override
	public String getString() {
		return String.format("id= % 6d, time= %-18s, senderUid= % 12d, senderNickName= \"%-15s\", groupUid= % 10d, " +
						"groupName= \"%-18s\", content= \"%s\"", id, time.toString().replace("T", " "),
				senderUid, senderNickName, groupUid, groupName, content);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GroupMessage that = (GroupMessage) o;
		return new EqualsBuilder()
				.append(senderUid, that.senderUid)
				.append(groupUid, that.groupUid)
				.append(content, that.content)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(senderUid)
				.append(groupUid)
				.append(content)
				.toHashCode();
	}

	@Override
	public String toString() {
		return getString();
	}
}
