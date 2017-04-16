package avalon.util;

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
    private final int id;
    private final String name;
    private List<GPlayer> players = new ArrayList<>();
    private final GPacket packet;

    public GGroup(int id, String name, GPacket packet) {
        this.id = id;
        this.name = name;
        this.packet = packet;
    }

    public GGroup(int id, String name, GPacket packet, List<GPlayer> players) {
        this(id, name, packet);
        this.players = players;
    }

    public GGroup(int id, String name, GPacket packet, GPlayer[] players) {
        this(id, name, packet);
        Collections.addAll(this.players, players);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public GGroup addPlayer(GPlayer player) {
        players.add(player);
        return this;
    }

    public List<GPlayer> getPlayers() {
        return players;
    }

    public GPacket getPacket() {
        return packet;
    }
}
