package data;

import util.GExit;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GameData {
    private static List<GExit> gameMap = new LinkedList<>();

    public static boolean addExit(GExit exit) {
        return gameMap.add(exit);
    }

    public static Stream<GExit> getGameMap() {
        return gameMap.stream();
    }
}
