package test;

import avalon.model.ExtendLanguage;
import avalon.model.ExtendSubmission;
import org.apache.commons.io.IOUtils;
import org.verwandlung.voj.judger.core.Compiler;
import org.verwandlung.voj.judger.core.Preprocessor;
import org.verwandlung.voj.judger.core.Runner;

import java.io.File;
import java.io.FileReader;
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
public class ExecutiveTest {

    public static void main(String[] args) throws Exception {
        String workDirectory = System.getProperty("java.io.tmpdir") + "executive";
        String code = "#include <cstdio>\n" +
                "\n" +
                "int main() {\n" +
                "\tprintf(\"Hello World\");\n" +
                "\treturn 0;\n" +
                "}";
        ExtendSubmission submission = new ExtendSubmission(code,
                new ExtendLanguage("C++11",
                        "g++ -O2 -Wall -std=c++11 -o {filename}.exe {filename}.cpp -lm",
                        "{filename}.exe"));
        String baseFileName = String.valueOf(submission.getSubmitTime());
        String out = workDirectory + File.separator + "out#" + submission.getSubmitTime() + ".txt";
        Compiler compiler = new Compiler();
        Runner runner = new Runner();
        Preprocessor preprocessor = new Preprocessor();
        preprocessor.createTestCode(submission, workDirectory, baseFileName);

        Map<String, Object> processResult = compiler.getCompileResult(submission, workDirectory, baseFileName);
        Map<String, Object> result = new HashMap<>(2, 2);

        FileReader reader;
        boolean successful = (boolean) processResult.get("isSuccessful");
        if (successful) {
            processResult.putAll(runner.getRuntimeResult(submission, workDirectory, baseFileName, out));
            reader = new FileReader(out);
        } else
            reader = new FileReader((String) processResult.get("compileLogPath"));
        result.put("error", !successful);
        result.put("out/error", IOUtils.toString(reader).trim());
        result.put("command", ((String) processResult.get("commandLine")).trim());
        reader.close();
        // clean
        List<Path> paths = Files.list(Paths.get(workDirectory)).collect(toList());
        for (Path thisPath : paths)
            Files.deleteIfExists(thisPath);
    }
}
