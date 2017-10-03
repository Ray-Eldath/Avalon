package avalon.api;

import avalon.util.GroupConfig;
import avalon.util.GroupResponderPermission;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupConfigUtils {
	private static Map<String, long[]> cache = new HashMap<>();

	public static long[] getAllowArray(GroupConfig config, String id) {
		if (cache.containsKey(id))
			return cache.get(id);

		List<GroupResponderPermission> permissions = config.getPermissions();
		for (GroupResponderPermission thisPermission : permissions) {
			if (thisPermission.getIdentifier().equals(id)) {
				long[] r = ArrayUtils.addAll(thisPermission.getAllow(), config.getAdmin());
				cache.put(id, r);
				return r;
			}
		}
		return null;
	}
}
