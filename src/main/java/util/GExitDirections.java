package util;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
@SuppressWarnings("WeakerAccess")
public class GExitDirections {
    public static final GExitDirection UP = new GExitDirection("上", "下");
    public static final GExitDirection DOWN = new GExitDirection("下", "上");
    public static final GExitDirection LEFT = new GExitDirection("左", "右");
    public static final GExitDirection RIGHT = new GExitDirection("右", "左");

    public static GExitDirection getOpposite(GExitDirection input) {
        if (input.equals(UP)) return DOWN;
        else if (input.equals(DOWN)) return UP;
        else if (input.equals(LEFT)) return RIGHT;
        return RIGHT;
    }
}
