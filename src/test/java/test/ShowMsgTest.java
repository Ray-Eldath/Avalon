package test;

import avalon.extend.Scheduler;
import avalon.extend.ShowMsg;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Eldath Ray on 2017/6/18 0018.
 *
 * @author Eldath Ray
 */
public class ShowMsgTest {
    public static void main(String[] args) {
        new ShowMsg();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new Scheduler(), 1, 5, TimeUnit.SECONDS);
    }
}
