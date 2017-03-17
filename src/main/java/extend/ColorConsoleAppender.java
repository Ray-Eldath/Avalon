package extend;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.rmi.dgc.Lease;

/**
 * Created by Eldath Ray on 2017/3/12 0012.
 *
 * @author Eldath Ray
 */
public class ColorConsoleAppender extends AppenderSkeleton{

    @Override
    protected void append(LoggingEvent event) {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
