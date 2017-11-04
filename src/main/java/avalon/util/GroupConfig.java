package avalon.util;

import java.util.List;

public class GroupConfig {
	private boolean listen, record;
	private long owner;
	private long[] admin, blacklist;
	private List<GroupResponderConfigEntry> permissions;

	public GroupConfig(boolean listen,
	                   boolean record,
	                   long owner,
	                   long[] admin,
	                   long[] blacklist,
	                   List<GroupResponderConfigEntry> permissions) {
		this.listen = listen;
		this.record = record;
		this.owner = owner;
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

	public long getOwner() {
		return owner;
	}

	public long[] getAdmin() {
		return admin;
	}

	public long[] getBlacklist() {
		return blacklist;
	}

	public List<GroupResponderConfigEntry> getPermissions() {
		return permissions;
	}
}
