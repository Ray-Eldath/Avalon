package main;

import api.MainServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.Response;

import java.net.InetSocketAddress;
import java.util.Scanner;

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
            for (String thisFollowFollow : MainServlet.followGroup)
                Response.responseGroup(thisFollowFollow, "服务已经停止。");
        }
    }

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new atShutdownDo());
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8088);
        Server server = new Server(address);
        ServletContextHandler context =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        server.setStopAtShutdown(true);
        context.addServlet(new ServletHolder(new MainServlet()), "/");
        server.start();
        logger.info("Is server on (yes or no): ");
        Scanner scanner = new Scanner(System.in);
        String isOn = scanner.nextLine();
        if (isOn.toLowerCase().equals("yes"))
            for (String thisFollowGroup : MainServlet.followGroup) {
                Response.responseGroup(thisFollowGroup, "Avalon已经上线。");
                Response.responseGroup(thisFollowGroup, "惩罚性严格过滤已经启用。");
            }
        else logger.info("Cancel send login message.");
        logger.info("Server now running!");
        server.join();
    }
}
