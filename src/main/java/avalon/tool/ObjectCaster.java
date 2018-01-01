package avalon.tool;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/3/23.
 *
 * @author Eldath Ray
 */
public class ObjectCaster {
	public static long toLong(Object object) {
		return object instanceof Long ? (long) object : (long) (int) object;
	}

	public static long[] toLongArray(Object[] objects) {
		if (objects.length == 0)
			return ArrayUtils.EMPTY_LONG_ARRAY;
		long[] result = new long[objects.length];
		Object object;
		for (int i = 0; i < objects.length; i++) {
			object = objects[i];
			result[i] = toLong(object);
		}
		return result;
	}

	public static long[] toLongArray(Collection<Object> objects) {
		return toLongArray(objects.toArray());
	}

	public static Collection<String> toStringList(Collection<Object> objects) {
		List<String> result = new ArrayList<>(objects.size());
		for (Object thisObject : objects)
			result.add((String) thisObject);
		return result;
	}

	public static String[] toStringArray(Object[] objects) {
		String[] result = new String[objects.length];
		for (int i = 0; i < objects.length; i++)
			result[i] = (String) objects[i];
		return result;
	}

	public static JSONObject[] toJSONObjectArray(Object[] objects) {
		JSONObject[] result = new JSONObject[objects.length];
		for (int i = 0; i < objects.length; i++)
			result[i] = (JSONObject) objects[i];
		return result;
	}
}
