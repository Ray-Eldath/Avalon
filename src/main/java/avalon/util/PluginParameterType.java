package avalon.util;

/**
 * Created by Eldath Ray on 2017/4/20 0020.
 *
 * @author Eldath Ray
 */
public enum PluginParameterType {
    UNKNOWN, INT, INT_SECOND, LONG, STRING, STRING_EMAIL, STRING_IP_ADDRESS, TURE_FLASE, URL, FILE_PATH, ARRAY, SEQ;

    public static PluginParameterType from(String name) {
        if (name.contains("$$Seq:"))
            return PluginParameterType.SEQ;
        else if (name.contains("$$Array:"))
            return PluginParameterType.ARRAY;
        else if ("$$Int".equals(name))
            return PluginParameterType.INT;
        else if ("$$Int:Second".equals(name))
            return PluginParameterType.INT_SECOND;
        else if ("$$Long".equals(name))
            return PluginParameterType.LONG;
        else if ("$$String".equals(name))
            return PluginParameterType.STRING;
        else if ("$$String:Email".equals(name))
            return PluginParameterType.STRING_EMAIL;
        else if ("$$String:IPAddress".equals(name))
            return PluginParameterType.STRING_IP_ADDRESS;
        else if ("$$TrueFalse".equals(name))
            return PluginParameterType.TURE_FLASE;
        else if ("$$URL".equals(name))
            return PluginParameterType.URL;
        else if ("$$FilePath".equals(name))
            return PluginParameterType.FILE_PATH;
        else return PluginParameterType.UNKNOWN;
    }

    public static boolean isParameters(String name) {
        return isParameters(from(name));
    }

    public static boolean isParameters(PluginParameterType type) {
        return type == PluginParameterType.SEQ || type == PluginParameterType.ARRAY;
    }
}
