package avalon.main;

import avalon.tool.ProcessHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static avalon.tool.ConstantPool.Basic.currentPath;

/**
 * Created by Eldath Ray on 2017/4/19 0019.
 *
 * @author Eldath Ray
 */
public class InstallChecker {
    private static final Logger logger = LoggerFactory.getLogger(InstallChecker.class);

    public static boolean check() {
        try {
            Process wechat = new ProcessHolder(Runtime.getRuntime().exec("perl " + currentPath +
                    File.separator + "bin" + File.separator + "Mojo-Weixin.pl"), "from perl:Mojo-Weixin").getProcess();
            if (!checkProcess(wechat)) {
                logger.error("Can't locate Mojo-Weixin! Please sure you're install Mojo-Webqq " +
                        "with the steps in https://github.com/sjdy521/Mojo-Weixin#安装方法 !");
                System.exit(-3);
            } else MainServer.setWechatProcess(wechat);
            System.out.println("Webqq!");
            //
            Process webqq = new ProcessHolder(Runtime.getRuntime().exec("perl " + currentPath +
                    File.separator + "bin" + File.separator + "Mojo-Webqq.pl"), "from perl:Mojo-Webqq").getProcess();
            if (!checkProcess(webqq)) {
                logger.error("Can't locate Mojo-Webqq! Please sure you're install Mojo-Webqq " +
                        "with the steps in https://github.com/sjdy521/Mojo-Webqq#安装方法 !");
                System.exit(-3);
            } else MainServer.setWebqqProcess(webqq);
            return true;
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
