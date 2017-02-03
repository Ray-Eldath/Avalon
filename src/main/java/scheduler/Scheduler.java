package scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Eldath on 2017/1/31 0031.
 *
 * @author Eldath
 */
public class Scheduler extends Thread {
    private static TreeMap<LocalDateTime, Task> allTask = new TreeMap<>();
    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    static void scheduleTask(LocalDateTime runTime, Task doWhat) {
        allTask.put(runTime, doWhat);
        logger.info("Task " + doWhat.getClass().getName() + " is now scheduled.");
    }

    @Override
    public void run() {
        flush();
    }

    private static void flush() {
        LocalDateTime now = LocalDateTime.now();
        if (allTask.isEmpty()) return;
        Map.Entry<LocalDateTime, Task> thisEntry = allTask.firstEntry();
        LocalDateTime localDateTime = thisEntry.getKey();
        Task toShow = thisEntry.getValue();
        if (now.getDayOfYear() > localDateTime.getDayOfYear()) {
            allTask.remove(localDateTime);
        } else if (now.isEqual(localDateTime)) {
            toShow.run();
            allTask.remove(localDateTime);
        }
    }
}
