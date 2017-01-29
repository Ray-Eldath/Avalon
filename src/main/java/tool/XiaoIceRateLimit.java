package tool;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class XiaoIceRateLimit {
    private long lastTime = System.currentTimeMillis();
    private long lastAllowTime = System.currentTimeMillis() + 3L;
    private static XiaoIceRateLimit instance = null;

    public static XiaoIceRateLimit getInstance() {
        if (instance == null) instance = new XiaoIceRateLimit();
        return instance;
    }

    public void setLastAllowTime(long lastAllowTime) {
        this.lastAllowTime = lastAllowTime;
    }

    public boolean isLimitNeeded(long thisTime) {
        return thisTime <= lastAllowTime;
    }
}
