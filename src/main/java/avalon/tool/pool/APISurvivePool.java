package avalon.tool.pool;

import avalon.api.GroupMessageResponderHolder;
import avalon.group.GroupMessageResponder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APISurvivePool {
    private Map<GroupMessageResponder, Boolean> survive = new HashMap<>();
    private Map<GroupMessageResponder, Boolean> noticed = new HashMap<>();

    private static APISurvivePool instance = new APISurvivePool();

    public static APISurvivePool getInstance() {
        return instance;
    }

    public boolean containAPI(GroupMessageResponder input) {
        return survive.containsKey(input);
    }

    public void addAPI(GroupMessageResponderHolder input) {
        survive.put(input.getResponder(), true);
        noticed.put(input.getResponder(), false);
    }

    public void setAPISurvive(GroupMessageResponder input, boolean state) {
        survive.put(input, state);
    }

    public boolean isSurvive(GroupMessageResponder input) {
        return survive.get(input);
    }

    public boolean isNoticed(GroupMessageResponder input) {
        return noticed.get(input);
    }

    public void setNoticed(GroupMessageResponder api) {
        noticed.put(api, true);
    }
}
