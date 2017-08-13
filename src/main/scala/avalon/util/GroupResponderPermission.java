package avalon.util;

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
}
