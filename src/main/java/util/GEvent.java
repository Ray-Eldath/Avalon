package util;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GEvent {
    private GEventTrigger trigger;
    private Queue<GSection> sections = new ArrayDeque<>();

    public GEvent(GEventTrigger trigger, Queue<GSection> sections) {
        this.trigger = trigger;
        this.sections = sections;
    }

    public GEvent(GEventTrigger trigger, GSection... sections) {
        this.trigger = trigger;
        Collections.addAll(this.sections, sections);
    }

    public GEventTrigger getTrigger() {
        return trigger;
    }

    public GSection poll() {
        return sections.poll();
    }
}
