package util;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GEventTriggers {
    public static class FREE_MODE extends GEventTrigger {
        public FREE_MODE() {
        }
    }

    public static class MEET_NPC extends GEventTrigger {
        private int meetTime;
        private GNpc target;

        public MEET_NPC(int meetTime, GNpc target) {
            this.meetTime = meetTime;
            this.target = target;
        }

        public int getMeetTime() {
            return meetTime;
        }

        public GNpc getTarget() {
            return target;
        }
    }

    public static class PICK_ITEM extends GEventTrigger {
        private GItem item;

        public PICK_ITEM(GItem item) {
            this.item = item;
        }

        public GItem getItem() {
            return item;
        }
    }

    public static class INTO_ROOM extends GEventTrigger {
        private GRoom room;

        public INTO_ROOM(GRoom room) {
            this.room = room;
        }

        public GRoom getRoom() {
            return room;
        }
    }
}
