package util;

import java.util.ArrayList;
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

    public GPacket() {
        this.items = new ArrayList<>();
    }

    public List<GItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (GItem thisItem : items)
            stringBuilder.append(thisItem.toString()).append("\n");
        return "背包中的物品有\n：" + stringBuilder.toString();
    }
}
