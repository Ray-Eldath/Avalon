package avalon.main;

import avalon.tool.pool.ConstantPool;
import avalon.util.ProcessHolder;
import avalon.util.servlet.MojoWebqqServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static avalon.tool.pool.ConstantPool.Basic.currentPath;
import static avalon.tool.pool.ConstantPool.Basic.currentServlet;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
class InstallChecker {
	private static final Logger logger = LoggerFactory.getLogger(InstallChecker.class);

	static void check() {
		try {
			handleLockFile(ConstantPool.Address.perlFileOfWechat);
			String prefix = "perl \"" + currentPath + File.separator + "bin" + File.separator;
//            System.out.println("prefix: " + prefix);
//            System.out.println("Mojo-Weixin.pl: " + prefix + "Mojo-Weixin.pl");
			ProcessHolder wechatHolder = new ProcessHolder(Runtime.getRuntime().exec(
					prefix + "Mojo-Weixin.pl\""), "from perl:Mojo-Weixin ");
			wechatHolder.start();
			Process wechat = wechatHolder.getProcess();
			if (wechat == null) {
				logger.error("Fatal error: Mojo-Weixin not run currently!");
				System.exit(-3);
			}
			if (currentServlet instanceof MojoWebqqServlet) {
				handleLockFile(ConstantPool.Address.servletScriptFile);
				ProcessHolder webqqHolder = new ProcessHolder(Runtime.getRuntime().exec(
						prefix + "Mojo-Webqq.pl\""), "from perl:Mojo-Webqq ");
				webqqHolder.start();
				Process webqq = webqqHolder.getProcess();
				if (webqq == null) {
					logger.error("Fatal error: Mojo-Webqq not run currently!");
					System.exit(-3);
				}
				//
				if (!checkProcess(webqq)) {
					logger.error("Can't locate Mojo-Webqq! Please sure you're install Mojo-Webqq " +
							"with the steps in https://github.com/sjdy521/Mojo-Webqq#安装方法 !");
					System.exit(-3);
				} else MainServer.setWebqqProcess(webqq);
				if (!checkProcess(wechat)) {
					logger.error("Can't locate Mojo-Weixin! Please sure you're install Mojo-Webqq " +
							"with the steps in https://github.com/sjdy521/Mojo-Weixin#安装方法 !");
					System.exit(-3);
				} else MainServer.setWechatProcess(wechat);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void handleLockFile(String filePath) {
		try {
			Path lockFilePath = Paths.get(filePath + ".lock");
			Path path = Paths.get(filePath);
			if (Files.exists(lockFilePath)) {
				Files.delete(path);
				if (!lockFilePath.toFile().renameTo(path.toFile()))
					throw new IOException("Operation not compete: Unknown reason.");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean checkProcess(Process process) throws IOException {
		ExecutorService pool = Executors.newFixedThreadPool(2);
		Future<String> future = pool.submit(() ->
				new BufferedReader(new InputStreamReader(process.getErrorStream())).readLine());
		String thisLine;
		try {
			thisLine = future.get(2, TimeUnit.SECONDS);
		} catch (Exception e) {
			return true;
		}
		return !thisLine.trim().toLowerCase().contains("can't locate");
	}
}
