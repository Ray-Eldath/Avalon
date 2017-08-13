package avalon.tool;

import org.apache.commons.lang3.ArrayUtils;

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

	public static long[] toLongArray(List<Object> objects) {
		return toLongArray(objects.toArray());
	}

	public static String[] toStringArray(Object[] objects) {
		String[] result = new String[objects.length];
		for (int i = 0; i < objects.length; i++)
			result[i] = (String) objects[i];
		return result;
	}
}
