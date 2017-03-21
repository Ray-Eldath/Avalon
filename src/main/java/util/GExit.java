package util;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GExit {
    private GRoom to;
    private GExitDirection exitDirection;
    private boolean reversible;

    public GExit(GRoom to, GExitDirection exitDirection, boolean reversible) {
        this.to = to;
        this.exitDirection = exitDirection;
        this.reversible = reversible;
    }

    public GExit(GRoom to, GExitDirection exitDirection) {
        this(to, exitDirection, false);
    }

    public GRoom getToRoom() {
        return to;
    }

    public GExitDirection getExitDirection() {
        return exitDirection;
    }

    public boolean isReversible() {
        return reversible;
    }
}
