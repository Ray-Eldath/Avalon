package avalon.util;

import java.util.List;

public class GroupConfig {
	private boolean listen, record;
	private long[] admin, blacklist;
	private List<GroupResponderPermission> permissions;

	public GroupConfig(boolean listen, boolean record, long[] admin, long[] blacklist, List<GroupResponderPermission> permissions) {
		this.listen = listen;
		this.record = record;
		this.admin = admin;
		this.blacklist = blacklist;
		this.permissions = permissions;
	}

	public boolean isListen() {
		return listen;
	}

	public boolean isRecord() {
		return record;
	}

	public long[] getAdmin() {
		return admin;
	}

	public long[] getBlacklist() {
		return blacklist;
	}

	public List<GroupResponderPermission> getPermissions() {
		return permissions;
	}
}
