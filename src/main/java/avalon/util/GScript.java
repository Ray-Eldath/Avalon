package avalon.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Eldath Ray on 2017/3/25 0025.
 *
 * @author Eldath Ray
 */
public class GScript implements Solidifiable, Displayable {
    private int id;
    private String name, describe;
    private List<GPath> paths = new ArrayList<>();
    private List<GNpc> gNPCs = new ArrayList<>();
    private List<GEvent> events = new ArrayList<>();
    //
    private Stream<GPath> pathStream;
    private Stream<GNpc> npcStream;
    private Stream<GEvent> eventStream;

    public GScript(int id, String name, String describe) {
        this.id = id;
        this.name = name;
        this.describe = describe;
    }

    @Override
    public void solidify() {
        pathStream = paths.stream();
        npcStream = gNPCs.stream();
        eventStream = events.stream();
        paths = null;
        gNPCs = null;
        events = null;
    }

    @Override
    public String getString() {
        return "剧本" + id + " - " + name + "：" + describe;
    }

    public boolean addPath(GPath path) {
        return paths.add(path);
    }

    public boolean addNPC(GNpc npc) {
        return gNPCs.add(npc);
    }

    public boolean addEvent(GEvent section) {
        return events.add(section);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescribe() {
        return describe;
    }

    public Stream<GPath> getPaths() {
        return pathStream;
    }

    public Stream<GNpc> getNpcs() {
        return npcStream;
    }

    public Stream<GEvent> getEvents() {
        return eventStream;
    }
}
