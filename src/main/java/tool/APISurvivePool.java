package tool;

import command.GroupMessageAPI;

import java.util.HashMap;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APISurvivePool {
    private static HashMap<GroupMessageAPI, Boolean> survive;
    private HashMap<GroupMessageAPI, Boolean> noticed;
    private static APISurvivePool instance = null;

    public static APISurvivePool getInstance() {
        if (instance == null) instance = new APISurvivePool();
        return instance;
    }

    private APISurvivePool() {
        survive = new HashMap<>();
        noticed = new HashMap<>();
    }

    public boolean containAPI(GroupMessageAPI input) {
        return survive.containsKey(input);
    }

    public void addAPI(GroupMessageAPI input) {
        survive.put(input, true);
        noticed.put(input, false);
    }

    public void setAPISurvive(GroupMessageAPI input, boolean state) {
        survive.put(input, state);
    }

    public boolean isSurvive(GroupMessageAPI input) {
        return survive.get(input);
    }

    public boolean isNoticed(GroupMessageAPI input) {
        return noticed.get(input);
    }

    public void setNoticed(GroupMessageAPI api) {
        noticed.put(api, true);
    }
}
