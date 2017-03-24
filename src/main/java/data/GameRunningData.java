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
        private static final List<GGroup> groups = new ArrayList<>();
        private static final List<GPlayer> players = new ArrayList<>();

        public static List<GPlayer> getPlayers() {
            return players;
        }

        public static boolean newPlayer(GPlayer player) {
            totalPlayer++;
            return players.add(player);
        }

        public static boolean isPlayerExist(long id) {
            return players.stream().anyMatch((player -> player.getId() == id));
        }

        public static boolean isPlayerExist(String playerName) {
            return players.stream().anyMatch((player -> player.getName().equals(playerName)));
        }

        public static GPlayer getPlayer(long uid) {
            return players.stream().filter((player -> player.getId() == uid)).findFirst().orElse(null);
        }

        public static boolean newGroup(GGroup group) {
            totalGroup++;
            return groups.add(group);
        }

        public static GGroup getGroup(int id) {
            return groups.stream().filter((group) -> group.getId() == id).findFirst().orElse(null);
        }

        public static GGroup getGroup(String name) {
            return groups.stream().filter((group) -> group.getName().equals(name)).findFirst().orElse(null);
        }

        public static boolean isGroupExist(String groupName) {
            return groups.stream().anyMatch((group) -> groupName.equals(group.getName()));
        }

        public static boolean isGroupExist(int groupId) {
            return groups.stream().anyMatch((group) -> groupId == group.getId());
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
