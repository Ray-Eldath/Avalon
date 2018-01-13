package avalon.tool.system;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

import static avalon.tool.pool.Constants.Address.DATA_PATH;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class RunningData implements BaseConfigSystem {
	private static final Path FILE = Paths.get(DATA_PATH + File.separator + "data.json");
	private static final Logger LOGGER = LoggerFactory.getLogger(RunningData.class);
	private static JSONObject object = new JSONObject();

	private static RunningData instance = null;

	public static RunningData getInstance() {
		if (instance == null) instance = new RunningData();
		return instance;
	}

	private RunningData() {
		if (Files.notExists(FILE)) {
			object.put("friend_id", 0);
			object.put("group_id", 0);
			object.put("group_message_recorded_count", 0);
			object.put("friend_message_recorded_count", 0);
			save();
		}
		try {
			object = (JSONObject) new JSONTokener(Files.newBufferedReader(FILE)).nextValue();
		} catch (IOException e) {
			LOGGER.error("exception thrown while read running data file: " + FILE.toString() + ": `" + e.getLocalizedMessage() + "`");
		}
	}

	public void set(String key, String value) {
		object.put(key, value);
	}

	public void set(String key, Object value) {
		object.put(key, value);
	}

	public void set(String key, Map<?, ?> value) {
		object.put(key, value);
	}

	public void set(String key, Collection<?> value) {
		object.put(key, value);
	}

	@Override
	public String getString(String key) {
		return object.getString(key);
	}

	public int getInt(String key) {
		return object.getInt(key);
	}

	public JSONArray getJSONArray(String key) {
		return object.getJSONArray(key);
	}

	public JSONObject getJSONObject(String key) {
		return object.getJSONObject(key);
	}

	@Override
	public Object get(String key) {
		return object.get(key);
	}

	public void save() {
		try (BufferedWriter writer = Files.newBufferedWriter(FILE)) {
			object.write(writer);
		} catch (IOException e) {
			LOGGER.error("exception thrown while save running data file: " + FILE.toString() + ": `" + e.getLocalizedMessage() + "`");
		}
	}
}
