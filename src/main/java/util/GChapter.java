package util;

import java.util.stream.Stream;

/**
 * Created by Eldath Ray on 2017/3/25 0025.
 *
 * @author Eldath Ray
 */
public class GChapter extends GSection {
    private int id;
    private String name, describe;
    private Stream<GEvent> eventStream;
    //private static char[] chinese = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十'};


    public GChapter(int id, String name, String describe) {
        this.id = id;
        this.name = name;
        this.describe = describe;
    }

    public void setEvents(Stream<GEvent> eventStream) {
        this.eventStream = eventStream;
    }

    public Stream<GEvent> getEvents() {
        return eventStream;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescribe() {
        return describe;
    }

    @Override
    public String getString() {
        return "第" + id + "章：" + name + "\n" + describe;
    }
}
