package avalon.util;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GroupResponderPermission {
	private String identifier;
	private long[] allow;

	public GroupResponderPermission(String identifier, long[] allow) {
		this.identifier = identifier;
		this.allow = allow;
	}

	public String getIdentifier() {
		return identifier;
	}

	public long[] getAllow() {
		return allow;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("identifier", identifier)
				.append("allow", allow)
				.toString();
	}
}
