package avalon.main;

import avalon.extend.Recorder;
import avalon.extend.Scheduler;
import avalon.extend.ShowMsg;
import avalon.friend.MainFriendMessageHandler;
import avalon.group.MainGroupMessageHandler;
import avalon.info.*;
import avalon.manager.InstanceManager;
import avalon.tool.ConfigSystem;
import avalon.tool.DelayResponse;
import avalon.tool.RunningData;
import avalon.tool.pool.AvalonPluginPool;
import avalon.tool.pool.ConstantPool;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static avalon.tool.pool.ConstantPool.Basic.currentServlet;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServer {
    private static final Logger logger = LoggerFactory.getLogger(MainServer.class);
    private static long[] followGroup = MainGroupMessageHandler.getInstance().getFollowGroup();
    private static Process webqqProcess, wechatProcess;

    static class atShutdownDo extends Thread {
        @Override
        public void run() {
            logger.info("Catch INT signal, Bye!");
            Recorder.getInstance().flushNow();
            RunningData.getInstance().save();
            //
            for (long thisFollowFollow : followGroup)
                currentServlet.responseGroup(thisFollowFollow, "服务已经停止。");
            currentServlet.shutdown();
            ConstantPool.Database.currentDatabaseOperator.close();
            currentServlet.clean();
        }
    }

    public static void main(String[] args) throws Exception {
        ConfigSystem.getInstance();
        RunningData.getInstance();
        new ConstantPool.Basic();
        new ConstantPool.Address();
        AvalonPluginPool.getInstance().load();
        if (!ConstantPool.Basic.Debug)
            InstallChecker.check();
        // 线程池
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
        poolExecutor.scheduleWithFixedDelay(new Scheduler(), 5, 5, TimeUnit.SECONDS);
        // 初始化Task
        new ShowMsg();
        // 关车钩子
        Runtime.getRuntime().addShutdownHook(new atShutdownDo());
        InetSocketAddress address;
        final String addressString = currentServlet.listenAddress().replace("http://", "");
        if (!addressString.contains(":"))
            address = new InetSocketAddress(addressString, 80);
        else {
            final String[] addressStringArray = addressString.split(":");
            address = new InetSocketAddress(addressStringArray[0].replace("//", ""),
                    Integer.parseInt(addressStringArray[1]));
        }
        //
        Server server = new Server(address);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/avalon/v0");
        server.setHandler(context);
        server.setStopAtShutdown(true);

        ConstantPool.Basic.currentServlet.setGroupMessageReceivedHook(e -> MainGroupMessageHandler.getInstance().handle(e));
        ConstantPool.Basic.currentServlet.setFriendMessageReceivedHook(e -> MainFriendMessageHandler.getInstance().handle(e));

        String[] tmp = currentServlet.listenAddress().replace("http://", "").split("/");
        context.addServlet(new ServletHolder(currentServlet), tmp.length == 1 ? "" : tmp[1]);
        context.addServlet(new ServletHolder(new WebqqPluginInfo()), "/info/get_webqq_plugin_info");
        context.addServlet(new ServletHolder(new ClientVersion()), "/info/get_client_version");
        context.addServlet(new ServletHolder(new ClientStatus()), "/info/get_client_status");
        context.addServlet(new ServletHolder(new AvalonInfo()), "/info/get_avalon_info");
        context.addServlet(new ServletHolder(new SystemInfo()), "/info/get_system_info");
        context.addServlet(new ServletHolder(new InstanceManager()), "/manager/manage_instance");
        server.join();
        server.start();
        logger.info("Is server on (yes or no): ");
        Scanner scanner = new Scanner(System.in);
        String isOn = scanner.nextLine();
        if ("yes".equals(isOn.toLowerCase()))
            for (long thisFollowGroup : followGroup)
                currentServlet.responseGroup(thisFollowGroup, "Avalon已经上线。");
        else logger.info("Cancel send login message.");
        DelayResponse delayResponse = new DelayResponse();
        delayResponse.start();
        logger.info("DelayResponse thread is loaded.");
        logger.info("Server now running!");
        logger.info("IMPORTANCE: Please exit this system by pressed Ctrl-C, DO NOT close this window directly!");
    }

    public static Process getWebqqProcess() {
        return webqqProcess;
    }

    public static Process getWechatProcess() {
        return wechatProcess;
    }

    public static void setWebqqProcess(Process webqqProcess) {
        MainServer.webqqProcess = webqqProcess;
    }

    public static void setWechatProcess(Process wechatProcess) {
        MainServer.wechatProcess = wechatProcess;
    }
}