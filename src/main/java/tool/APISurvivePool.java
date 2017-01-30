package tool;

import api.API;
import api.Blacklist;

import java.util.HashMap;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APISurvivePool {
    private static HashMap<API, Boolean> survive;
    private HashMap<API, Boolean> noticed;
    private static APISurvivePool instance = null;

    public static APISurvivePool getInstance() {
        if (instance == null) instance = new APISurvivePool();
        return instance;
    }

    private APISurvivePool() {
        survive = new HashMap<>();
        noticed = new HashMap<>();
    }

    public boolean containAPI(API input) {
        return survive.containsKey(input);
    }

    public void addAPI(API input) {
        survive.put(input, true);
        noticed.put(input, false);
    }

    public void setAPISurvive(API input, boolean state) {
        survive.put(input, state);
    }

    public boolean isSurvive(API input) {
        return survive.get(input);
    }

    public boolean isNoticed(API input) {
        return noticed.get(input);
    }

    public void setNoticed(API api) {
        noticed.put(api, true);
    }
}
