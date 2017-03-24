package util;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
@SuppressWarnings("WeakerAccess")
public class GPathDirections {
    public static final GPathDirection UP = new GPathDirection("上", "下");
    public static final GPathDirection DOWN = new GPathDirection("下", "上");
    public static final GPathDirection LEFT = new GPathDirection("左", "右");
    public static final GPathDirection RIGHT = new GPathDirection("右", "左");

    public static GPathDirection getOpposite(GPathDirection input) {
        if (input.equals(UP)) return DOWN;
        else if (input.equals(DOWN)) return UP;
        else if (input.equals(LEFT)) return RIGHT;
        return RIGHT;
    }
}
