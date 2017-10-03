package avalon.servlet.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/4/20 0020.
 *
 * @author Eldath Ray
 */
public class MojoWebqqPluginParameter {
	private MojoWebqqPluginParameterType type;
	private MojoWebqqPluginParameterType elementType;
	private String name;

	private static List<MojoWebqqPluginParameter> childParameter = new ArrayList<>();

	public MojoWebqqPluginParameter(String name, MojoWebqqPluginParameterType type, MojoWebqqPluginParameterType... elementType) {
		this.type = type;
        this.name = name;
        if (elementType != null && elementType.length > 0) {
	        if (!(type == MojoWebqqPluginParameterType.ARRAY || type == MojoWebqqPluginParameterType.SEQ))
		        throw new UnsupportedOperationException("Only ARRAY or SEQ has element!");
            else if (elementType.length != 1)
                throw new UnsupportedOperationException("Only one elementType!");
            else this.elementType = elementType[0];
        }
    }

	public MojoWebqqPluginParameter addChildParameter(MojoWebqqPluginParameter parameter) {
		childParameter.add(parameter);
        return this;
    }

	public List<MojoWebqqPluginParameter> getChildParameter() {
		return childParameter;
    }

    public String getName() {
        return name;
    }

	public MojoWebqqPluginParameterType getType() {
		return type;
    }

	public MojoWebqqPluginParameterType getElementType() {
		if (elementType == null)
            throw new UnsupportedOperationException("No elementType!");
        return elementType;
    }

    public boolean isCollention() {
        return elementType == null;
    }
}
