package tool;

/**
 * Created by Eldath Ray on 2017/3/23.
 *
 * @author Eldath Ray
 */
public class ObjectArrayCaster {
    public static long[] toLong(Object[] objects) {
        long[] result = new long[objects.length];
        for (int i = 0; i < objects.length; i++)
            result[i] = (long) (int) objects[i];
        return result;
    }
}
