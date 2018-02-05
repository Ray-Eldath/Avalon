package avalon.tool.system;

import avalon.group.GroupMessageHandler;
import avalon.group.GroupMessageResponder;
import avalon.group.GroupMessageResponderTriple;
import avalon.tool.ObjectCaster;
import avalon.tool.pool.Constants;
import avalon.util.GroupConfig;
import avalon.util.GroupResponderConfigEntry;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GroupConfigs {
	private Map<Long, avalon.util.GroupConfig> configs = new HashMap<>();
	private List<Long> followGroups = new ArrayList<>();

	private static GroupConfigs instance = null;

	public static GroupConfigs instance() {
		if (instance == null)
			try {
				instance = new GroupConfigs();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		return instance;
	}

	private GroupConfigs() throws IOException {
		List<String> allIdentifier = new ArrayList<>();
		for (GroupMessageResponder responder :
				GroupMessageHandler.INSTANCE.getApiTriples()
						.stream()
						.map(GroupMessageResponderTriple::getInstance)
						.collect(Collectors.toList())) {
			String[] identifier = responder.responderInfo().getConfigIdentifier();
			if (identifier != null)
				Collections.addAll(allIdentifier, identifier);
		}

		String file = Constants.Basic.INSTANCE.getCURRENT_PATH() + File.separator + "group.json";
		JSONArray root = ((JSONObject) new JSONTokener(Files.newBufferedReader(Paths.get(file))).nextValue())
				.getJSONObject(Constants.Basic.INSTANCE.getCURRENT_SERVLET().name())
				.getJSONArray("group");
		for (int i = 0; i < root.length(); i++) {
			JSONObject thisObject = root.getJSONObject(i);
			long uid = ObjectCaster.toLong(thisObject.get("uid"));
			boolean listen = thisObject.getBoolean("listen");
			long owner = ObjectCaster.toLong(thisObject.get("owner"));
			List<Object> adminListT = thisObject.getJSONArray("admin").toList();
			adminListT.add(Constants.Basic.DEBUG_MESSAGE_UID);
			adminListT.add(owner);
			Set<Object> adminList = new HashSet<>(adminListT); // 去重

			long[] admin = ObjectCaster.toLongArray(adminList);

			if (listen)
				followGroups.add(uid);
			List<GroupResponderConfigEntry> permission = new ArrayList<>();
			if (!thisObject.has("permission")) {
				for (String thisIdentifier : allIdentifier)
					permission.add(new GroupResponderConfigEntry(thisIdentifier, admin));
			} else
				permission = parsePermission(thisObject.getJSONArray("permission"), allIdentifier, admin);
			configs.put(uid, new GroupConfig(
					listen,
					thisObject.getBoolean("record"),
					owner,
					admin,
					thisObject.has("blacklist") ?
							ObjectCaster.toLongArray(thisObject.getJSONArray("blacklist").toList()) : new long[]{},
					permission));
		}
	}

	private List<GroupResponderConfigEntry> parsePermission(JSONArray source, Collection<String> allIdentifier, long[] admins) {
		List<GroupResponderConfigEntry> result = new ArrayList<>();
		List<String> currentIdentifier = new ArrayList<>();

		for (int i = 0; i < source.length(); i++) {
			JSONObject thisObject = source.getJSONObject(i);
			String thisId = thisObject.getString("identifier");
			currentIdentifier.add(thisId);
			long[] thisAllow = ObjectCaster.toLongArray(thisObject.getJSONArray("allow").toList());
			result.add(new GroupResponderConfigEntry(thisId, thisAllow));
		}

		for (String thisIdentifier : allIdentifier)
			if (!currentIdentifier.contains(thisIdentifier))
				result.add(new GroupResponderConfigEntry(thisIdentifier, admins));

		return result;
	}

	public avalon.util.GroupConfig getConfig(long uid) {
		return configs.get(uid);
	}

	public List<Long> getFollowGroups() {
		return followGroups;
	}
}
