package avalon.main;

import avalon.api.DelayResponse;
import avalon.extend.*;
import avalon.friend.FriendMessageHandler;
import avalon.group.GroupMessageHandler;
import avalon.servlet.info.*;
import avalon.servlet.manager.InstanceManager;
import avalon.tool.pool.AvalonPluginPool;
import avalon.tool.pool.Constants;
import avalon.tool.system.Config;
import avalon.tool.system.GroupConfig;
import avalon.tool.system.RunningData;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static avalon.tool.pool.Constants.Basic.currentServlet;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class MainServer {
	private static final Logger logger = LoggerFactory.getLogger(MainServer.class);
	private static List<Long> followGroup = GroupConfig.instance().getFollowGroups();
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
			Constants.Database.currentDatabaseOperator.close();
			currentServlet.clean();
		}
	}

	public static void main(String[] args) throws Exception {
		// 字符集处理
		System.setProperty("file.encoding", "UTF-8");
		// debug检测
		if (Constants.Basic.debug)
			logger.warn("Avalon is running under DEBUG mode!");
//		if (!currentServlet.test()) {
//			logger.error("can not connect to servlet " + currentServlet.name() + "! please check this servlet is DO running...");
//			System.exit(-1);
//		}
		// 响应速度太慢。

		// FIXME 感觉可以删掉，因为是自动加载的
		Config.Companion.instance();
		RunningData.getInstance();
		new Constants.Basic();
		new Constants.Address();
		AvalonPluginPool.INSTANCE.load();
		// 线程池
		new ShowMsg();
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
		executor.scheduleAtFixedRate(new Scheduler(), 6, 5, TimeUnit.SECONDS);

		if (Constants.Setting.RSS_Enabled)
			executor.scheduleAtFixedRate(RSSFeeder.getInstance(), 2, 10, TimeUnit.MINUTES);
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

		currentServlet.setGroupMessageReceivedHook(e -> GroupMessageHandler.getInstance().handle(e));
		currentServlet.setFriendMessageReceivedHook(FriendMessageHandler.INSTANCE::handle);

		context.addServlet(new ServletHolder(currentServlet), "/post_url");
		context.addServlet(new ServletHolder(new WebqqPluginInfo()), "/info/get_webqq_plugin_info");
		context.addServlet(new ServletHolder(new ClientVersion()), "/info/get_client_version");
		context.addServlet(new ServletHolder(new ClientStatus()), "/info/get_client_status");
		context.addServlet(new ServletHolder(new AvalonInfo()), "/info/get_avalon_info");
		context.addServlet(new ServletHolder(new SystemInfo()), "/info/get_system_info");
		context.addServlet(new ServletHolder(new InstanceManager()), "/manager/manage_instance");
		server.join();
		server.start();

		logger.info("Is server on (y or n, default n, await for 5 seconds): ");
		int isOn = readInput();
		if (isOn == 1) {
			for (long thisFollowGroup : followGroup) {
				String str = "Avalon已经上线。\n发送`avalon help`以获取帮助信息。";
				Object config = Config.INSTANCE.getCommandConfig("Hitokoto", "push_when_start");
				if (config != null && (boolean) config)
					str += "\n\n" + Hitokoto.INSTANCE.get();
				currentServlet.responseGroup(thisFollowGroup, str);
			}
			logger.info("Login message sent.");
		} else if (isOn == 0)
			logger.info("Cancel send login message.");
		else
			logger.info("Invalid input or reached the maximum waiting time, use default value: `n`.");
		DelayResponse delayResponse = new DelayResponse();
		delayResponse.start();
		logger.info("DelayResponse thread is loaded.");
		logger.info("Server now running!");
		logger.info("IMPORTANCE: Please exit this system by pressed Ctrl-C, DO NOT close this window directly!");
	}

	private static int readInputStreamWithTimeout(InputStream is, byte[] buf) throws IOException, InterruptedException {
		int bufferOffset = 0;
		long maxTimeMillis = System.currentTimeMillis() + 5000;
		while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < buf.length) {
			int readLength = Math.min(is.available(), buf.length - bufferOffset);
			int readResult = is.read(buf, bufferOffset, readLength);
			if (readResult == -1)
				break;
			bufferOffset += readResult;
			if (readResult > 0)
				break;
			Thread.sleep(10);
		}
		return bufferOffset;
	}


	private static int readInput() throws IOException, InterruptedException {
		int result = 0;
		byte[] inputData = new byte[1];
		int readLength = readInputStreamWithTimeout(System.in, inputData);
		if (readLength > 0) {
			switch ((char) inputData[0]) {
				case 'y':
				case 'Y': {
					result = 1;
					break;
				}
			}
		} else
			result = -1;
		return result;
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