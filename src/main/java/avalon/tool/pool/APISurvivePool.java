package avalon.tool.pool;

import avalon.group.BaseGroupMessageResponder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APISurvivePool {
    private static Map<BaseGroupMessageResponder, Boolean> survive = new HashMap<>();
    private final Map<BaseGroupMessageResponder, Boolean> noticed = new HashMap<>();
    private static APISurvivePool instance = null;

    public static APISurvivePool getInstance() {
        if (instance == null) instance = new APISurvivePool();
        return instance;
    }

    public boolean containAPI(BaseGroupMessageResponder input) {
        return survive.containsKey(input);
    }

    public void addAPI(BaseGroupMessageResponder input) {
        survive.put(input, true);
        noticed.put(input, false);
    }

    public void setAPISurvive(BaseGroupMessageResponder input, boolean state) {
        survive.put(input, state);
    }

    public boolean isSurvive(BaseGroupMessageResponder input) {
        return survive.get(input);
    }

    public boolean isNoticed(BaseGroupMessageResponder input) {
        return noticed.get(input);
    }

    public void setNoticed(BaseGroupMessageResponder api) {
        noticed.put(api, true);
    }
}
