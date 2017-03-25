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
    public static final GPathDirection FRONT = new GPathDirection("前", "后");
    public static final GPathDirection BACK = new GPathDirection("后", "前");

    public static GPathDirection getOpposite(GPathDirection input) {
        if (input.equals(UP)) return DOWN;
        else if (input.equals(DOWN)) return UP;
        else if (input.equals(LEFT)) return RIGHT;
        else if (input.equals(RIGHT)) return LEFT;
        else if (input.equals(FRONT)) return BACK;
        return BACK;
    }

    public static GPathDirection fromName(String name) {
        GPathDirection pathDirection;
        String nameLower = name.toLowerCase();
        switch (nameLower) {
            case "left":
                pathDirection = GPathDirections.LEFT;
                break;
            case "right":
                pathDirection = GPathDirections.RIGHT;
                break;
            case "up":
                pathDirection = GPathDirections.UP;
                break;
            case "down":
                pathDirection = GPathDirections.DOWN;
                break;
            case "front":
                pathDirection = GPathDirections.FRONT;
                break;
            case "back":
                pathDirection = GPathDirections.BACK;
                break;
            default:
                pathDirection = null;
        }
        return pathDirection;
    }
}
