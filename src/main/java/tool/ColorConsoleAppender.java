package tool;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Created by Eldath Ray on 2017/3/12 0012.
 *
 * @author Eldath Ray
 */
public class ColorConsoleAppender extends AppenderSkeleton {

    public ColorConsoleAppender() {
        AnsiConsole.systemInstall();
    }

    @Override
    protected void append(LoggingEvent event) {
        Level level = event.getLevel();
        String message = event.getRenderedMessage();
        if (level == Level.WARN)
            System.out.println(ansi().fg(YELLOW).a(message).reset());
        else if (level == Level.ERROR || level == Level.FATAL)
            System.out.println(ansi().fg(RED).a(message).reset());
        else
            System.out.println(message);
    }

    @Override
    public void close() {
        AnsiConsole.systemUninstall();
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}
