package main;

import api.Recorder;
import extend.scheduler.Scheduler;
import extend.scheduler.ShowMsg;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.Response;
import tool.SQLiteDatabaseOperator;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServer {
    private static Logger logger = LoggerFactory.getLogger(MainServer.class);

    static class atShutdownDo extends Thread {
        @Override
        public void run() {
            MainServer.logger.info("Do cleaning job...");
            for (long thisFollowFollow : MainServlet.followGroup)
                Response.responseGroup(thisFollowFollow, "服务已经停止。");
            Recorder.getInstance().flushNow();
            SQLiteDatabaseOperator.getInstance().closeResource();
        }
    }

    public static void main(String[] args) throws Exception {
        // 线程池
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
        poolExecutor.scheduleWithFixedDelay(new Scheduler(), 5, 5, TimeUnit.SECONDS);
        // 初始化Task
        new ShowMsg();
        // 关车钩子
        Runtime.getRuntime().addShutdownHook(new atShutdownDo());
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8088);
        Server server = new Server(address);
        ServletContextHandler context =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        server.setStopAtShutdown(true);
        context.addServlet(new ServletHolder(new MainServlet()), "/");
        server.start();
        //
        logger.info("Is server on (yes or no): ");
        Scanner scanner = new Scanner(System.in);
        String isOn = scanner.nextLine();
        if ("yes".equals(isOn.toLowerCase()))
            for (long thisFollowGroup : MainServlet.followGroup)
                Response.responseGroup(thisFollowGroup, "Avalon已经上线。\nAvalon现存在一个与膜蛤插件有关的可能影响" +
                        "群内交流的bug，开发者目前仍未能有办法修复。若因此bug影响群内正常交流，请使用APIManager（具体请见帮助）" +
                        "关闭膜蛤插件。抱歉。");
        else logger.info("Cancel send login message.");
        logger.info("Server now running!");
        server.join();
    }
}
