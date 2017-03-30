package util;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/2/18 0018.
 *
 * @author Eldath
 */
public interface BaseResponder {
    String getHelpMessage();
    Pattern getKeyWordRegex();
}
