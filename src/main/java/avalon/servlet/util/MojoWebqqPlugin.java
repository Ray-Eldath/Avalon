package avalon.servlet.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/4/20 0020.
 *
 * @author Eldath Ray
 */
public class MojoWebqqPlugin {
    private int id;
    private String name, description, developer;
	private List<MojoWebqqPluginParameter> parameters = new ArrayList<>();

	public MojoWebqqPlugin(int id, String name, String description, String developer) {
		this.id = id;
        this.name = name;
        this.description = description;
        this.developer = developer;
    }

	public MojoWebqqPlugin addParameter(MojoWebqqPluginParameter parameter) {
		this.parameters.add(parameter);
        return this;
    }

	public MojoWebqqPlugin addParameter(List<MojoWebqqPluginParameter> parameter) {
		this.parameters.addAll(parameter);
        return this;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDeveloper() {
        return developer;
    }

	public List<MojoWebqqPluginParameter> getParameters() {
		return parameters;
    }
}
