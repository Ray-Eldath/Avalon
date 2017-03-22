package util;

import java.util.stream.Stream;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
@SuppressWarnings("WeakerAccess")
public class GGroup {
    private int id;
    private Stream<GPlayer> players;
    private GPacket packet;

    public GGroup(int id, Stream<GPlayer> players, GPacket packet) {
        this.id = id;
        this.players = players;
        this.packet = packet;
    }

    public GGroup(int id, GPlayer[] players, GPacket packet) {
        this(id, Stream.of(players), packet);
    }

    public int getId() {
        return id;
    }

    public Stream<GPlayer> getPlayers() {
        return players;
    }

    public GPacket getPacket() {
        return packet;
    }
}
