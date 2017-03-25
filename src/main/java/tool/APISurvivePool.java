package tool;

import command.BaseGroupMessageCommandRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APISurvivePool {
    private static Map<BaseGroupMessageCommandRunner, Boolean> survive;
    private final Map<BaseGroupMessageCommandRunner, Boolean> noticed;
    private static APISurvivePool instance = null;

    public static APISurvivePool getInstance() {
        if (instance == null) instance = new APISurvivePool();
        return instance;
    }

    private APISurvivePool() {
        survive = new HashMap<>();
        noticed = new HashMap<>();
    }

    public boolean containAPI(BaseGroupMessageCommandRunner input) {
        return survive.containsKey(input);
    }

    public void addAPI(BaseGroupMessageCommandRunner input) {
        survive.put(input, true);
        noticed.put(input, false);
    }

    public void setAPISurvive(BaseGroupMessageCommandRunner input, boolean state) {
        survive.put(input, state);
    }

    public boolean isSurvive(BaseGroupMessageCommandRunner input) {
        return survive.get(input);
    }

    public boolean isNoticed(BaseGroupMessageCommandRunner input) {
        return noticed.get(input);
    }

    public void setNoticed(BaseGroupMessageCommandRunner api) {
        noticed.put(api, true);
    }
}
