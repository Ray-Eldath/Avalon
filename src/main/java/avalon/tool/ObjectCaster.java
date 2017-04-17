package avalon.tool;

/**
 * Created by Eldath Ray on 2017/3/23.
 *
 * @author Eldath Ray
 */
public class ObjectCaster {
    public static long[] toLongArray(Object[] objects) {
        long[] result = new long[objects.length];
        Object object;
        for (int i = 0; i < objects.length; i++) {
            object = objects[i];
            result[i] = object instanceof Long ? (long) object : (long) (int) object;
        }
        return result;
    }

    public static String[] toStringArray(Object[] objects) {
        String[] result = new String[objects.length];
        for (int i = 0; i < objects.length; i++)
            result[i] = (String) objects[i];
        return result;
    }
}
