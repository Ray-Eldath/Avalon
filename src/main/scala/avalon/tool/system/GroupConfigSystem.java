package avalon.tool.system;

import avalon.tool.ObjectCaster;
import avalon.tool.pool.ConstantPool;
import avalon.util.GroupConfig;
import avalon.util.GroupResponderPermission;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupConfigSystem {
	private Map<Long, GroupConfig> configs = new HashMap<>();
	private List<Long> followGroups = new ArrayList<>();

	private static GroupConfigSystem instance = null;

	public static GroupConfigSystem instance() {
		if (instance == null)
			try {
				instance = new GroupConfigSystem();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		return instance;
	}

	private GroupConfigSystem() throws IOException {
		String file = ConstantPool.Basic.currentPath + File.separator + "group.json";
		JSONArray root = ((JSONObject) new JSONTokener(Files.newBufferedReader(Paths.get(file))).nextValue()).getJSONArray("group");
		for (int i = 0; i < root.length(); i++) {
			JSONObject thisObject = root.getJSONObject(i);
			long uid = ObjectCaster.toLong(thisObject.get("uid"));
			boolean listen = thisObject.getBoolean("listen");
			if (listen)
				followGroups.add(uid);
			List<GroupResponderPermission> permission = parsePermission(thisObject.getJSONArray("permission"));
			configs.put(uid, new GroupConfig(
					listen,
					thisObject.getBoolean("record"),
					ObjectCaster.toLongArray(thisObject.getJSONArray("admin").toList()),
					ObjectCaster.toLongArray(thisObject.getJSONArray("blacklist").toList()),
					permission));
			// TODO 缺少“未定义权限，将使用默认值：仅允许管理员”的检查。
		}
	}

	private List<GroupResponderPermission> parsePermission(JSONArray source) {
		List<GroupResponderPermission> result = new ArrayList<>();
		for (int i = 0; i < source.length(); i++) {
			JSONObject thisObject = source.getJSONObject(i);
			String thisId = thisObject.getString("identifier");
			long[] thisAllow = ObjectCaster.toLongArray(thisObject.getJSONArray("allow").toList());
			result.add(new GroupResponderPermission(thisId, thisAllow));
		}
		return result;
	}

	public GroupConfig getConfig(long uid) {
		return configs.get(uid);
	}

	public List<Long> getFollowGroups() {
		return followGroups;
	}
}
