package api;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/2/18 0018.
 *
 * @author Eldath
 */
public interface BaseAPI {
    String getHelpMessage();
    Pattern getKeyWordRegex();
}
