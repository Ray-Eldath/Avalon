package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
@SuppressWarnings("WeakerAccess")
public class GGroup {
    private int id;
    private List<GPlayer> players = new ArrayList<>();
    private GPacket packet;

    private void basic(int id, GPacket packet) {
        this.id = id;
        this.packet = packet;
    }

    public GGroup(int id, GPacket packet, List<GPlayer> players) {
        basic(id, packet);
        this.players = players;
    }

    public GGroup(int id, GPacket packet, GPlayer[] players) {
        basic(id, packet);
        Collections.addAll(this.players, players);
    }

    public int getId() {
        return id;
    }

    public boolean addPlayer(GPlayer player) {
        return players.add(player);
    }

    public List<GPlayer> getPlayers() {
        return players;
    }

    public GPacket getPacket() {
        return packet;
    }
}
