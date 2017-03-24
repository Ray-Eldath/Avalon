package util;

import java.util.Collections;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GRoom {
    private final int id;
    private boolean birthplace;
    private final String name;
    private final String describe;
    private List<GItemSet> itemSets;
    private List<GPath> exits;

    private GRoom(int id, String name, String describe) {
        this.id = id;
        this.name = name;
        this.describe = describe;
    }

    public GRoom(int id, String name, String describe, List<GItemSet> itemSets) {
        this(id, name, describe);
        this.itemSets = itemSets;
    }

    public GRoom(int id, String name, String describe, GItemSet[] itemSets) {
        this(id, name, describe);
        Collections.addAll(this.itemSets, itemSets);
    }

    public void setExits(List<GPath> exits) {
        this.exits = exits;
    }

    public boolean addExit(GPath exit) {
        return this.exits.add(exit);
    }

    public boolean addExit(GPath[] exitArray) {
        return Collections.addAll(this.exits, exitArray);
    }

    public boolean addExit(List<GPath> exitList) {
        return this.exits.addAll(exitList);
    }

    public boolean isBirthplace() {
        return birthplace;
    }

    public void setBirthplace(boolean birthplace) {
        this.birthplace = birthplace;
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

    public List<GItemSet> getItemSets() {
        return itemSets;
    }

    public List<GPath> getExits() {
        return this.exits;
    }


}
