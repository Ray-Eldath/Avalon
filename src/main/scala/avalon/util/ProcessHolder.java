package avalon.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Eldath Ray on 2017/4/18 0018.
 *
 * @author Eldath Ray
 */
public class ProcessHolder extends Thread {
    private Process process;
    private String[] prefix;

    public ProcessHolder(Process process, String... prefix) {
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
                    System.out.println((thisLine));
            else
                while ((thisLine = reader.readLine()) != null)
                    System.out.println(prefix[0] + thisLine);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Process getProcess() {
        return process;
    }
}
