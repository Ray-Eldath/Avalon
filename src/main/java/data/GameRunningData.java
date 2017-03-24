package data;

import util.GGroup;
import util.GPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/3/24.
 *
 * @author Eldath Ray
 */
public class GameRunningData {
    public static class GroupAndPlayer {
        private static int totalGroup = 0, totalPlayer = 0;
        private static List<GGroup> groups = new ArrayList<>();
        private static List<GPlayer> players = new ArrayList<>();

        public static List<GPlayer> getPlayers() {
            return players;
        }

        public static boolean newPlayer(GPlayer player) {
            totalPlayer++;
            return players.add(player);
        }

        public static boolean newGroup(GGroup group) {
            totalGroup++;
            return groups.add(group);
        }

        public static boolean joinGroup(GGroup group, GPlayer player) {
            if (!groups.contains(group)) return false;
            groups.remove(group);
            return groups.add(group.addPlayer(player));
        }

        public static List<GGroup> getGroups() {
            return groups;
        }

        public static int getTotalGroup() {
            return totalGroup;
        }

        public static int getTotalPlayer() {
            return totalPlayer;
        }
    }
}
