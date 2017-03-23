package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
@SuppressWarnings("WeakerAccess")
public class GPlayer extends GPerson {
    private GRoom location;
    private GPacket personalPacket;
    private List<GEvent> activatedEvents = new ArrayList<>();
    private Map<GNpc, Integer> meetNpc = new HashMap<>();

    public GPlayer(long id, String name) {
        super(id, name);
    }

    public GRoom getLocation() {
        return location;
    }

    public GPacket getPersonalPacket() {
        return personalPacket;
    }

    public void setLocation(GRoom location) {
        this.location = location;
    }

    public void setPersonalPacket(GPacket personalPacket) {
        this.personalPacket = personalPacket;
    }

    public List<GEvent> getActivatedEvents() {
        return activatedEvents;
    }

    public boolean addActivatedEvent(GEvent event) {
        return this.activatedEvents.add(event);
    }

    public boolean delActivatedEvent(GEvent event) {
        return this.activatedEvents.remove(event);
    }

    public Map<GNpc, Integer> getMeetNpc() {
        return meetNpc;
    }

    public int getNpcMeetTime(GNpc npc) {
        return this.meetNpc.get(npc);
    }

    public void setNpcMeetTime(GNpc npc, int time) {
        meetNpc.put(npc, time);
    }

    public void plusNpcMeetTime(GNpc npc) {
        int oldValue = getNpcMeetTime(npc);
        setNpcMeetTime(npc, ++oldValue);
    }
}
