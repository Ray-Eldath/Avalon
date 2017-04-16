package avalon.util;

import avalon.data.GameData;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GPath {
    private final GRoom from;
    private final GRoom to;
    private final GPathDirection exitDirection;
    private final boolean reversible;
    private final int moveDelaySecond;

    private GPath(GRoom from, GRoom to, GPathDirection exitDirection, int moveDelaySecond, boolean reversible) {
        this.from = from;
        this.to = to;
        this.exitDirection = exitDirection;
        this.reversible = reversible;
        this.moveDelaySecond = moveDelaySecond;
        GameData.Map.addPath(this);
        if (reversible)
            GameData.Map.addPath(new GPath(to, from, GPathDirections.getOpposite(exitDirection), moveDelaySecond));
    }

    public GPath(GRoom from, GRoom to, GPathDirection exitDirection, int moveDelaySecond) {
        this(from, to, exitDirection, moveDelaySecond, false);
    }

    public GRoom getFromRoom() {
        return from;
    }

    public GRoom getToRoom() {
        return to;
    }

    public GPathDirection getExitDirection() {
        return exitDirection;
    }

    public boolean isReversible() {
        return reversible;
    }

    public int getMoveDelaySecond() {
        return moveDelaySecond;
    }
}
