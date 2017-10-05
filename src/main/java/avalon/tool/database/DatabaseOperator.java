package avalon.tool.database;

import avalon.util.FriendMessage;
import avalon.util.GroupMessage;

import java.time.LocalDateTime;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public interface DatabaseOperator {
	boolean initDB();

	boolean add(GroupMessage input);

	boolean add(FriendMessage input);

	boolean addQuote(LocalDateTime time, String speaker, String content);

	void close();
}
