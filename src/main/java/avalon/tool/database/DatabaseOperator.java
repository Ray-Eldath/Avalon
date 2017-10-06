package avalon.tool.database;

import avalon.util.FriendMessage;
import avalon.util.GroupMessage;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public interface DatabaseOperator {
	boolean initDB();

	boolean add(GroupMessage input);

	boolean add(FriendMessage input);

	boolean addQuote(int hashCode, String speaker, String content);

	boolean exist(String table, String condition);

	void close();
}
