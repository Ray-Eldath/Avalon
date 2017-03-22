package util;

import java.util.List;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GPacket {
    private List<GItem> items;

    public GPacket(List<GItem> items) {
        this.items = items;
    }

    public List<GItem> getItems() {
        return items;
    }
}
