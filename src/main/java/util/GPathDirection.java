package util;

/**
 * Created by Eldath Ray on 2017/3/22.
 *
 * @author Eldath Ray
 */
@SuppressWarnings("WeakerAccess")
public class GPathDirection {
    private final String name;
    private final String opposite;

    public GPathDirection(String name, String opposite) {
        this.name = name;
        this.opposite = opposite;
    }

    public String getName() {
        return name;
    }

    public String getOppositeName() {
        return opposite;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GPathDirection && getName().equals(((GPathDirection) o).getName());
    }
}
