package test;

import scheduler.Scheduler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TreeMap;

/**
 * Created by Eldath on 2017/1/31 0031.
 *
 * @author Eldath
 */
public class TreeMapTest {
    private static TreeMap<LocalTime, String> test = new TreeMap<>();

    public static void main(String[] args) {
        Scheduler.scheduleTask(LocalDateTime.parse("2017-01-30T16:02:10"), new doWhat());
        while (true)
            Scheduler.flush();
    }

    public static class doWhat implements Runnable {

        @Override
        public void run() {
            System.out.println("Nowtime: " + LocalDateTime.now() + "\tRunning!");
        }
    }
}
