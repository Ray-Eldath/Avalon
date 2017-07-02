package avalon.model;

import avalon.tool.pool.ConstantPool;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by Eldath Ray on 2017/6/11 0011.
 *
 * @author Eldath Ray
 */
public class SafetyReadableWriter extends Writer {
    private static boolean full = false;
    private StringBuilder builder = new StringBuilder("");

    public String out() {
        String r = builder.toString();
        builder = new StringBuilder("");
        return r;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (builder.length() + len > ConstantPool.Setting.Max_Stream_Length) {
            if (!full) {
                builder.append("...");
                full = true;
            }
        } else
            builder.append(cbuf, off, len).append("\n");
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }
}
