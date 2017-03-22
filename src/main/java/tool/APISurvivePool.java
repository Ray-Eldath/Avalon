package tool;

import command.BaseGroupMessageCommand;

import java.util.HashMap;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APISurvivePool {
    private static HashMap<BaseGroupMessageCommand, Boolean> survive;
    private HashMap<BaseGroupMessageCommand, Boolean> noticed;
    private static APISurvivePool instance = null;

    public static APISurvivePool getInstance() {
        if (instance == null) instance = new APISurvivePool();
        return instance;
    }

    private APISurvivePool() {
        survive = new HashMap<>();
        noticed = new HashMap<>();
    }

    public boolean containAPI(BaseGroupMessageCommand input) {
        return survive.containsKey(input);
    }

    public void addAPI(BaseGroupMessageCommand input) {
        survive.put(input, true);
        noticed.put(input, false);
    }

    public void setAPISurvive(BaseGroupMessageCommand input, boolean state) {
        survive.put(input, state);
    }

    public boolean isSurvive(BaseGroupMessageCommand input) {
        return survive.get(input);
    }

    public boolean isNoticed(BaseGroupMessageCommand input) {
        return noticed.get(input);
    }

    public void setNoticed(BaseGroupMessageCommand api) {
        noticed.put(api, true);
    }
}
