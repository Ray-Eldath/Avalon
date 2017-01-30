package scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Properties;

/**
 * Created by Eldath on 2017/1/30 0030.
 *
 * @author Eldath
 */
public class ShowMsg extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ShowMsg.class);
    private Properties prop = new Properties();
    private static final String urlString = "";

    public ShowMsg() {
        try {
            prop = new Properties();
            prop.load(new URL(urlString).openStream());
        } catch (Exception e) {
            logger.warn("Exception while ShowMsg: ", e);
        }
    }

    @Override
    public void run() {

    }
}
