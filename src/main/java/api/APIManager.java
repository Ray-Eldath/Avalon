package api;

import org.json.JSONObject;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class APIManager implements API {
    private static APIManager instance = null;

    public APIManager getInstance() {
        if (instance == null) instance = new APIManager();
        return instance;
    }

    @Override
    public void doPost(JSONObject object) {
        String content = object.get("content").toString();
    }

    @Override
    public void response(String groupNumber) {

    }
}
