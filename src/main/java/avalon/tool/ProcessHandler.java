package avalon.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Eldath Ray on 2017/4/18 0018.
 *
 * @author Eldath Ray
 */
public class ProcessHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ProcessHandler.class);
    private Process process;
    private String[] prefix;

    public ProcessHandler(Process process, String... prefix) {
        this.process = process;
        this.prefix = prefix;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                process.getInputStream(), "GB2312"))) {

            String thisLine;
            if (prefix.length == 0)
                while ((thisLine = reader.readLine()) != null)
                    System.out.println(thisLine);
            else
                while ((thisLine = reader.readLine()) != null)
                    System.out.println(prefix[0] + thisLine);
        } catch (IOException e) {
            logger.warn("Exception thrown while handle process: " + e);
        }
    }
}
