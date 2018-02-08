package avalon.tool.database;

import avalon.util.FriendMessage;
import avalon.util.GroupMessage;
import org.jetbrains.annotations.NotNull;

import java.sql.Statement;

/**
 * Created by Eldath on 2017/2/11 0011.
 *
 * @author Eldath
 */
public interface DatabaseOperator {
	boolean initDB();

	boolean add(@NotNull GroupMessage input);

	boolean add(@NotNull FriendMessage input);

	boolean addQuote(int hashCode, @NotNull String speaker, @NotNull String content);

	boolean exist(@NotNull Table table, @NotNull String condition);

	int count(@NotNull Table table);

	void close();

	@NotNull
	Statement statement();
}
