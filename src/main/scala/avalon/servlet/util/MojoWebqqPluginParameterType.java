package avalon.servlet.util;

/**
 * Created by Eldath Ray on 2017/4/20 0020.
 *
 * @author Eldath Ray
 */
public enum MojoWebqqPluginParameterType {
    UNKNOWN, INT, INT_SECOND, LONG, STRING, STRING_EMAIL, STRING_IP_ADDRESS, TURE_FLASE, URL, FILE_PATH, ARRAY, SEQ;

	public static MojoWebqqPluginParameterType from(String name) {
		if (name.contains("$$Seq:"))
	        return MojoWebqqPluginParameterType.SEQ;
		else if (name.contains("$$Array:"))
	        return MojoWebqqPluginParameterType.ARRAY;
		else if ("$$Int".equals(name))
	        return MojoWebqqPluginParameterType.INT;
		else if ("$$Int:Second".equals(name))
	        return MojoWebqqPluginParameterType.INT_SECOND;
		else if ("$$Long".equals(name))
	        return MojoWebqqPluginParameterType.LONG;
		else if ("$$String".equals(name))
	        return MojoWebqqPluginParameterType.STRING;
		else if ("$$String:Email".equals(name))
	        return MojoWebqqPluginParameterType.STRING_EMAIL;
		else if ("$$String:IPAddress".equals(name))
	        return MojoWebqqPluginParameterType.STRING_IP_ADDRESS;
		else if ("$$TrueFalse".equals(name))
	        return MojoWebqqPluginParameterType.TURE_FLASE;
		else if ("$$URL".equals(name))
	        return MojoWebqqPluginParameterType.URL;
		else if ("$$FilePath".equals(name))
	        return MojoWebqqPluginParameterType.FILE_PATH;
		else return MojoWebqqPluginParameterType.UNKNOWN;
    }

    public static boolean isParameters(String name) {
        return isParameters(from(name));
    }

	public static boolean isParameters(MojoWebqqPluginParameterType type) {
		return type == MojoWebqqPluginParameterType.SEQ || type == MojoWebqqPluginParameterType.ARRAY;
	}
}
