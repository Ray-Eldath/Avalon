package group;

import util.BaseResponder;
import util.GroupMessage;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public abstract class BaseGroupMessageResponder implements BaseResponder {
    public abstract void doPost(GroupMessage message);

    public abstract String getHelpMessage();

    public abstract Pattern getKeyWordRegex();

    @Override
    public boolean equals(Object object) {
        return object != null &&
                object instanceof BaseGroupMessageResponder &&
                getKeyWordRegex().toString().equals(((BaseGroupMessageResponder) object).getKeyWordRegex().toString());
    }
}
