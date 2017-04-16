package avalon.extend;

import avalon.util.GRoom;
import avalon.util.GSection;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
class GameSectionDisplayer {
    public static void display(GSection section) {
    }

    public static String getRoomMsg(GRoom room) {
        final StringBuilder sb = new StringBuilder();
        sb.append("您现在在 ").append(room.getName()).append(" \n").append(room.getDescribe()).append("\n此处的出口有：");
        // Lambda, lambda, GO!
        room.getExits().forEach((exit) -> sb.append(exit.getExitDirection()).append(" "));
        return sb.toString();
    }
}
