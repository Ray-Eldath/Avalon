package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GItemSet {
    private final GItem parentItem;
    private List<GItem> sonItems = new ArrayList<>();

    public GItemSet(GItem parentItem, List<GItem> sonItems) {
        this.parentItem = parentItem;
        this.sonItems = sonItems;
    }

    public GItemSet(GItem parentItem, GItem[] sonItems) {
        this.parentItem = parentItem;
        Collections.addAll(this.sonItems, sonItems);
    }

    public GItem getParentItem() {
        return parentItem;
    }

    public List<GItem> getSonItems() {
        return sonItems;
    }
}
