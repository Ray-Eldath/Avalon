package scheduler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Eldath on 2017/1/31 0031.
 *
 * @author Eldath
 */
public class Scheduler {
    private static TreeMap<LocalDateTime, Runnable> allTask = new TreeMap<>();

    public static void scheduleTask(LocalDateTime runTime, Runnable doWhat) {
        allTask.put(runTime, doWhat);
    }

    public static void flush() {
        LocalDateTime now = LocalDateTime.now();
        if (allTask.isEmpty()) return;
        Map.Entry<LocalDateTime, Runnable> thisEntry = allTask.firstEntry();
        LocalDateTime localDateTime = thisEntry.getKey();
        Runnable toShow = thisEntry.getValue();
        if (now.getDayOfYear() > localDateTime.getDayOfYear())
            allTask.remove(localDateTime);
        else if (now.isAfter(localDateTime)) {
            toShow.run();
            allTask.remove(localDateTime);
        }
    }
}
