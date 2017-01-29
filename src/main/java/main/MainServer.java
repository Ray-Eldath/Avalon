package main;

import api.MainServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import tool.Response;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServer {
    public static void main(String[] args) throws Exception {
        //new URL("http://127.0.0.1:5000/openqq/send_group_message?uid=399863405&content=" + loginMessage)
        //        .openConnection().connect();
        InetSocketAddress address = new InetSocketAddress(InetAddress.getLoopbackAddress(), 8088);
        Server server = new Server(address);
        ServletContextHandler context =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        server.setStopAtShutdown(true);
        context.addServlet(new ServletHolder(new MainServlet()), "/post_api");
        server.start();
        System.out.print("Is server on (yes or no): ");
        Scanner scanner = new Scanner(System.in);
        String isOn = scanner.nextLine();
        if (isOn.toLowerCase().equals("yes"))
            for (String thisFollowGroup : MainServlet.followGroup)
                Response.responseGroup(thisFollowGroup, "Avalon已经上线。");
        else System.exit(-10000);
        System.out.println("Server running!");
        server.join();
    }
}
