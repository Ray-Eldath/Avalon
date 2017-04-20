package avalon.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/4/20 0020.
 *
 * @author Eldath Ray
 */
public class PluginParameter {
    private PluginParameterType type;
    private PluginParameterType elementType;
    private String name;

    private static List<PluginParameter> childParameter = new ArrayList<>();

    public PluginParameter(String name, PluginParameterType type, PluginParameterType... elementType) {
        this.type = type;
        this.name = name;
        if (elementType != null && elementType.length > 0) {
            if (!(type == PluginParameterType.ARRAY || type == PluginParameterType.SEQ))
                throw new UnsupportedOperationException("Only ARRAY or SEQ has element!");
            else if (elementType.length != 1)
                throw new UnsupportedOperationException("Only one elementType!");
            else this.elementType = elementType[0];
        }
    }

    public PluginParameter addChildParameter(PluginParameter parameter) {
        childParameter.add(parameter);
        return this;
    }

    public List<PluginParameter> getChildParameter() {
        return childParameter;
    }

    public String getName() {
        return name;
    }

    public PluginParameterType getType() {
        return type;
    }

    public PluginParameterType getElementType() {
        if (elementType == null)
            throw new UnsupportedOperationException("No elementType!");
        return elementType;
    }
}
