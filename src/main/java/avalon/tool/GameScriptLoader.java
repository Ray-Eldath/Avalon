package avalon.tool;

import avalon.data.GameData;
import avalon.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Eldath Ray on 2017/3/25 0025.
 *
 * @author Eldath Ray
 */
public class GameScriptLoader {
    private static GameScriptLoader instance = null;
    private static List<GScript> scripts = new ArrayList<>();
    private static List<String> scriptMessages = new ArrayList<>();
    private static GScript script = new GScript(0, "测试剧本", "做一个测试！");
    private static List<GRoom> birthPlaces = new ArrayList<>();

    static {
        GNpc npc = new GNpc(0, "测试");
        script.addNPC(npc);
        script.addEvent(new GEvent(new GEventTriggers.FREE_MODE(), new GChatBlock(new GChat(npc, "测试"))));
        GItem parent = new GItem(0, "A", "A Test");
        GItem[] items = new GItem[]{new GItem(1, "测试", "测试")};
        GRoom from = new GRoom(0, "A", "A Test", new GItemSet[]{new GItemSet(parent, items)});
        from.setBirthplace(true);
        script.addPath(new GPath(from, from, GPathDirections.BACK, 40));
        script.solidify();
    }

    private GameScriptLoader() {
        scripts.add(script);
    }

    public int getScriptSize() {
        return scripts.size();
    }

    public boolean containScript(int scriptId) {
        return scripts.stream().anyMatch(gScript -> gScript.getId() == scriptId);
    }

    public static GameScriptLoader getInstance() {
        if (instance == null) instance = new GameScriptLoader();
        return instance;
    }

    public boolean load(int id) {
        return load(scripts.stream().filter((gScript -> gScript.getId() == id)).findFirst().orElse(null));
    }

    public boolean load(GScript script) {
        if (script == null) return false;
        script.getPaths().filter(path -> path.getFromRoom().isBirthplace())
                .forEach(path -> birthPlaces.add(path.getFromRoom()));
        script.getPaths().filter(path -> path.getToRoom().isBirthplace())
                .forEach(path -> birthPlaces.add(path.getToRoom()));
        scriptMessages.add(script.getString());
        GameData.Map.setMap(script.getPaths());
        GameData.Map.solidify();
        GameData.Event.setEvents(script.getEvents());
        GameData.Event.solidify();
        VariablePool.GameMode.Is_Loaded = true;
        return true;
    }

    public GScript getScript(int scriptId) {
        return scripts.stream().filter(gScript -> gScript.getId() == scriptId).findFirst().orElse(null);
    }

    public static List<GRoom> getBirthPlaces() {
        return birthPlaces;
    }

    public List<String> getScriptMessages() {
        return scriptMessages;
    }

    public Stream<GScript> getScripts() {
        return scripts.stream();
    }
}
