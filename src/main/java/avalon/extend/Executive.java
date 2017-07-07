package avalon.extend;

import avalon.model.executive.ExecutiveLanguage;
import avalon.model.executive.ExecutiveSubmission;
import org.apache.commons.io.IOUtils;
import org.verwandlung.voj.judger.core.Compiler;
import org.verwandlung.voj.judger.core.Preprocessor;
import org.verwandlung.voj.judger.core.Runner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created by Eldath Ray on 2017/6/24 0024.
 *
 * @author Eldath Ray
 */
public class Executive {
    private static final String workDirectory = "G:" + File.separator + "executive";

    public static Map<String, Object> execute(ExecutiveLanguage lang, String code) throws Exception {
        ExecutiveSubmission submission = new ExecutiveSubmission(code, lang);
        String out = workDirectory + File.separator + "out#" + submission.getSubmitTime() + ".txt";
        String baseFileName = String.valueOf(submission.getSubmitTime());
        Compiler compiler = new Compiler();
        Runner runner = new Runner();
        Preprocessor preprocessor = new Preprocessor();
        preprocessor.createTestCode(submission, workDirectory, baseFileName);

        Map<String, Object> result = new HashMap<>(3, 2);
        Map<String, Object> processResult = compiler.getCompileResult(submission, workDirectory, baseFileName);

        String readerPath;
        boolean successful = (boolean) processResult.get("isSuccessful");
        if (successful) {
            processResult.putAll(runner.getRuntimeResult(submission, workDirectory, baseFileName, out));
            readerPath = out;
        } else
            readerPath = (String) processResult.get("compileLogPath");
        String errorOrOut;
        FileReader reader = new FileReader(readerPath);
        if (Files.size(Paths.get(readerPath)) > 420) {
            errorOrOut = "<output overflow>";
            successful = false;
        } else
            errorOrOut = IOUtils.toString(reader).trim();
        result.put("error", !successful);
        result.put("out/error", errorOrOut.isEmpty() ? "<empty>" : errorOrOut);
        result.put("command", ((String) processResult.get("commandLine")).trim());
        reader.close();
        // clean
        delAll(Files.list(Paths.get(workDirectory)).collect(toList()));
        return result;
    }

    private static void delAll(List<Path> paths) throws IOException {
        for (Path thisPath : paths) {
            if (Files.isDirectory(thisPath))
                delAll(Files.list(thisPath).collect(toList()));
            else
                Files.deleteIfExists(thisPath);
        }
    }
}
