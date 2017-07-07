package org.verwandlung.voj.judger.core;

import avalon.model.executive.ExecutiveSubmission;
import org.apache.logging.log4j.core.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 程序编译器, 用于编译用户提交的代码.
 *
 * @author Haozhe Xie
 */
public class Compiler {
    private Map<String, Object> result = new HashMap<>(10, 2);

    /**
     * 获取编译输出结果.
     *
     * @param submission    - 提交记录对象
     * @param workDirectory - 编译输出目录
     * @param baseFileName  - 编译输出文件名
     * @return 包含编译输出结果的 {@code Map<String, Object>} 对象
     */
    public Map<String, Object> getCompileResult(ExecutiveSubmission submission, String workDirectory, String baseFileName) {
        String commandLine = getCompileCommandLine(submission, workDirectory, baseFileName);
        String compileLogPath = getCompileLogPath(workDirectory, baseFileName);
        result.put("commandLine", commandLine);
        result.put("compileLogPath", compileLogPath);

        return getCompileResult(commandLine, compileLogPath);
    }

    /**
     * 获取编译命令.
     *
     * @param submission    - 提交记录对象
     * @param workDirectory - 编译输出目录
     * @param baseFileName  - 编译输出文件名
     * @return 编译命令
     */
    private String getCompileCommandLine(ExecutiveSubmission submission, String workDirectory, String baseFileName) {
        String filePathWithoutExtension = workDirectory + File.separator + baseFileName;
        return submission.getLanguage()
                .getCompileCommand()
                .replace("{filename}", filePathWithoutExtension);
    }

    /**
     * 获取编译日志输出的文件路径.
     *
     * @param workDirectory - 编译输出目录
     * @param baseFileName  - 编译输出文件名
     * @return 编译日志输出的文件路径
     */
    private String getCompileLogPath(String workDirectory, String baseFileName) {
        return String.format("%s/%s-compile.log", workDirectory, baseFileName);
    }

    /**
     * 获取编译输出结果.
     *
     * @param commandLine    - 编译命令
     * @param compileLogPath - 编译日志输出路径
     * @return 包含编译输出结果的 {@code Map<String, Object>} 对象
     */
    private Map<String, Object> getCompileResult(String commandLine, String compileLogPath) {
        int timeLimit = 1000;
        int memoryLimit = 65535;

        LOGGER.info("Start compiling with command: " + commandLine);
        Map<String, Object> runningResult = compilerRunner.getRuntimeResult(
                commandLine, compileLogPath, timeLimit, memoryLimit);

        boolean isSuccessful = false;
        if (runningResult != null) {
            int exitCode = (Integer) runningResult.get("exitCode");
            isSuccessful = exitCode == 0;
        }
        result.put("isSuccessful", isSuccessful);
        result.put("log", getCompileOutput(compileLogPath));
        return result;
    }

    /**
     * 获取编译日志内容.
     *
     * @param compileLogPath - 编译日志路径
     * @return 编译日志内容
     */
    private String getCompileOutput(String compileLogPath) {
        String compileLog = "";
        try {
            FileReader reader = new FileReader(compileLogPath);
            compileLog = IOUtils.toString(reader);
            reader.close();
        } catch (Exception ignore) {
        }
        return compileLog;
    }

    /**
     * 自动注入的Runner对象.
     * 用于执行编译命令.
     */
    private Runner compilerRunner = new Runner();

    /**
     * 日志记录器.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Compiler.class);
}