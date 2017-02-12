package extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ConstantPool;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Created by Eldath on 2017/2/3 0003.
 *
 * @author Eldath
 */
public class Recorder {
    private static ArrayList<String> recode = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(Recorder.class);
    private static final Path toSaved = Paths.get(System.getProperty("user.dir") + ConstantPool.recordSavePath);
    private static Recorder instance = null;

    public static Recorder getInstance() {
        if (instance == null) instance = new Recorder();
        return instance;
    }

    private Recorder() {
        try {
            if (Files.notExists(toSaved))
                Files.createFile(toSaved);
        } catch (IOException e) {
            logger.error("IOException in Recorder: ", e);
        }
    }

    public void recode(String content) {
        recode.add(content);
        flush();
    }

    private void flush() {
        try (BufferedWriter bw = Files.newBufferedWriter(toSaved, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            if (recode.size() > 10) {
                logger.info("Now flush record data...");
                for (String thisString : recode) {
                    bw.write(thisString);
                    bw.newLine();
                }
                bw.flush();
                recode.clear();
            }
        } catch (IOException e) {
            logger.error("IOException in Recorder: ", e);
        }
    }
}
