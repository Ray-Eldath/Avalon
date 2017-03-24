package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/3/20.
 *
 * @author Eldath Ray
 */
@SuppressWarnings("WeakerAccess")
public class GItem {
    private final int id;
    private final String name;
    private final String describe;
    private List<GReaction> reactions = new ArrayList<>();

    public GItem(int id, String name, String describe) {
        this.id = id;
        this.name = name;
        this.describe = describe;
    }

    public void setReactions(GReaction... inputReactions) {
        reactions.clear();
        addReactions(inputReactions);
    }

    public void setReactions(List<GReaction> inputReactions) {
        reactions = inputReactions;
    }

    public void addReaction(GReaction input) {
        this.reactions.add(input);
    }

    public void addReactions(GReaction... inputReactions) {
        Collections.addAll(this.reactions, inputReactions);
    }

    public void addReactions(List<GReaction> inputReactions) {
        this.reactions.addAll(inputReactions);
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

    public List<GReaction> getReactions() {
        return reactions;
    }

    @Override
    public String toString() {
        return "物品 No." + id + " " + name + "：" + describe;
    }
}
