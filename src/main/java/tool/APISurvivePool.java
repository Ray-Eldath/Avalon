package tool;

import command.GroupMessageCommand;

import java.util.HashMap;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APISurvivePool {
    private static HashMap<GroupMessageCommand, Boolean> survive;
    private HashMap<GroupMessageCommand, Boolean> noticed;
    private static APISurvivePool instance = null;

    public static APISurvivePool getInstance() {
        if (instance == null) instance = new APISurvivePool();
        return instance;
    }

    private APISurvivePool() {
        survive = new HashMap<>();
        noticed = new HashMap<>();
    }

    public boolean containAPI(GroupMessageCommand input) {
        return survive.containsKey(input);
    }

    public void addAPI(GroupMessageCommand input) {
        survive.put(input, true);
        noticed.put(input, false);
    }

    public void setAPISurvive(GroupMessageCommand input, boolean state) {
        survive.put(input, state);
    }

    public boolean isSurvive(GroupMessageCommand input) {
        return survive.get(input);
    }

    public boolean isNoticed(GroupMessageCommand input) {
        return noticed.get(input);
    }

    public void setNoticed(GroupMessageCommand api) {
        noticed.put(api, true);
    }
}
