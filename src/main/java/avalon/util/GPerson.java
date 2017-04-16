package avalon.util;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
public class GPerson {
    private final long Id;
    private final String name;

    GPerson(long id, String name) {
        Id = id;
        this.name = name;
    }

    public long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }
}
