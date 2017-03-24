package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GPacket {
    private final List<GItem> items;
    private int money;

    public GPacket(List<GItem> items) {
        this.items = items;
    }

    public GPacket() {
        this.items = new ArrayList<>();
    }

    public List<GItem> getItems() {
        return items;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void plusMoney(int money) {
        this.money += money;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (GItem thisItem : items)
            stringBuilder.append(thisItem.toString()).append("\n");
        return "金币总数：" + money + "个\n背包中的物品有\n：" + stringBuilder.toString();
    }
}
