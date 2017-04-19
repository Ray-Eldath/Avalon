package avalon.main;

import avalon.extend.Recorder;
import avalon.extend.Scheduler;
import avalon.extend.ShowMsg;
import avalon.group.MainGroupMessageHandler;
import avalon.info.*;
import avalon.manager.InstanceManager;
import avalon.tool.*;
import avalon.tool.database.SQLiteDatabaseOperator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static avalon.tool.ConstantPool.Address.webqq;
import static avalon.tool.ConstantPool.Address.wechat;

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
            SQLiteDatabaseOperator.getInstance().close();
            if (!ConstantPool.Basic.Debug) {
                webqqProcess.destroy();
                System.out.println("Mojo-Webqq exited, exit value: " + webqqProcess.exitValue());
                wechatProcess.destroy();
                System.out.println("Mojo-Weixin exited, exit value: " + wechatProcess.exitValue());
            }
            File[] files = new File(System.getProperty("java.io.tmpdir")).listFiles();
            if (files != null)
                Arrays.stream(files).filter(e -> e.getName().trim().matches("mojo_")).forEach(File::delete);
            logger.info("Mojo-Webqq files cleaned.");
            try {
                new URL(webqq + "/openqq/stop_client").openStream();
                new URL(wechat + "/openwx/stop_client").openStream();
            } catch (IOException ignored) {
            }
            for (long thisFollowFollow : followGroup)
                Responder.sendToGroup(thisFollowFollow, "服务已经停止。");
        }
    }

    public static void main(String[] args) throws Exception {
        ConfigSystem.getInstance();
        new ConstantPool.Basic();
        new ConstantPool.Address();
        if (!ConstantPool.Basic.Debug) InstallChecker.check();
        System.out.println(ConstantPool.Address.webqq);
        System.out.println(ConstantPool.Address.wechat);
        // 线程池
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
        poolExecutor.scheduleWithFixedDelay(new Scheduler(), 5, 5, TimeUnit.SECONDS);
        // 初始化Task
        new ShowMsg();
        // 关车钩子
        Runtime.getRuntime().addShutdownHook(new atShutdownDo());
        InetSocketAddress address;
        final String addressString = ((String) ConfigSystem.getInstance()
                .getConfig("Mojo-Webqq_POST_API_Address")).replace("http://", "");
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
        context.addServlet(new ServletHolder(new MainServlet()), "/post_api");
        context.addServlet(new ServletHolder(new WebqqPluginInfo()), "/info/get_webqq_plugin_info");
        context.addServlet(new ServletHolder(new ClientVersion()), "/info/get_client_version");
        context.addServlet(new ServletHolder(new ClientStatus()), "/info/get_client_status");
        context.addServlet(new ServletHolder(new AvalonInfo()), "/info/get_avalon_info");
        context.addServlet(new ServletHolder(new SystemInfo()), "/info/get_system_info");
        context.addServlet(new ServletHolder(new InstanceManager()), "/manager/manage_instance");
        //
        try {
            Runtime.getRuntime().exec("perl");
        } catch (Exception e) {
            logger.error("Perl has NOT install yet, please install perl (Linux: perl, Windows: ActivePerl) first!");
            System.exit(-1);
        }
        //
        server.join();
        server.start();
        logger.info("Is server on (yes or no): ");
        Scanner scanner = new Scanner(System.in);
        String isOn = scanner.nextLine();
        if ("yes".equals(isOn.toLowerCase()))
            for (long thisFollowGroup : followGroup)
                Responder.sendToGroup(thisFollowGroup, "Avalon已经上线。");
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