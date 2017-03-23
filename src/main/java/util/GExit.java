package util;

import data.GameData;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GExit {
    private GRoom from, to;
    private GExitDirection exitDirection;
    private boolean reversible;
    private int moveDelaySecond;

    public GExit(GRoom from, GRoom to, GExitDirection exitDirection, int moveDelaySecond, boolean reversible) {
        this.from = from;
        this.to = to;
        this.exitDirection = exitDirection;
        this.reversible = reversible;
        this.moveDelaySecond = moveDelaySecond;
        GameData.addExit(this);
        if (reversible)
            GameData.addExit(new GExit(to, from, GExitDirections.getOpposite(exitDirection), moveDelaySecond));
    }

    public GExit(GRoom from, GRoom to, GExitDirection exitDirection, int moveDelaySecond) {
        this(from, to, exitDirection, moveDelaySecond, false);
    }

    public GRoom getFromRoom() {
        return from;
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

    public int getMoveDelaySecond() {
        return moveDelaySecond;
    }
}
