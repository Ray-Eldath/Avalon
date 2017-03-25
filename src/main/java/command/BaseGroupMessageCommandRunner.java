package command;

import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public abstract class BaseGroupMessageCommandRunner implements BaseAPI {
    public abstract void doPost(GroupMessage message);

    public abstract String getHelpMessage();

    public abstract Pattern getKeyWordRegex();

    @Override
    public boolean equals(Object object) {
        return object != null &&
                object instanceof BaseGroupMessageCommandRunner &&
                getKeyWordRegex().toString().equals(((BaseGroupMessageCommandRunner) object).getKeyWordRegex().toString());
    }
}
