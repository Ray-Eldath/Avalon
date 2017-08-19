package avalon.api.util;

/**
 * Created by Eldath Ray on 2017/5/27 0027.
 *
 * @author Eldath Ray
 */
public class PluginInfo {
    private String name, version, copyright, website, classString, fileName;
    private boolean enabled;

	public PluginInfo(String name, String version, String copyright, String website, String classString, String fileName, boolean enabled) {
		this.name = name;
        this.version = version;
        this.copyright = copyright;
        this.website = website;
        this.classString = classString;
        this.fileName = fileName;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getWebsite() {
        return website;
    }

    public String getClassString() {
        return classString;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
