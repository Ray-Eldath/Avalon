package org.verwandlung.voj.judger.core;

import avalon.model.ExtendLanguage;
import avalon.model.ExtendSubmission;
import avalon.tool.system.ConfigSystem;
import avalon.util.NativeLibraryLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地程序执行器, 用于执行本地应用程序. 包括编译器(gcc)以及用户提交的代码所编译出的程序.
 *
 * @author Haozhe Xie
 */
public class Runner {

    /**
     * 获取(用户)程序运行结果.
     *
     * @param submission     - 评测记录对象
     * @param workDirectory  - 编译生成结果的目录以及程序输出的目录
     * @param baseFileName   - 待执行的应用程序文件名(不包含文件后缀)
     * @param outputFilePath - 输出文件路径
     * @return 一个包含程序运行结果的 {@code Map<String, Object>} 对象
     */
    public Map<String, Object> getRuntimeResult(ExtendSubmission submission,
                                                String workDirectory,
                                                String baseFileName,
                                                String outputFilePath) {
        String commandLine = getCommandLine(submission, workDirectory, baseFileName);
        int timeLimit = getTimeLimit(submission);
        int memoryLimit = getMemoryLimit(submission);
        Map<String, Object> result = new HashMap<>(5, 2);
        int usedTime = 0;
        int usedMemory = 0;
        int exitCode = -1;
        try {
            Map<String, Object> runtimeResult = getRuntimeResult(
                    commandLine, systemUsername, systemPassword, null, outputFilePath, timeLimit, memoryLimit);
            exitCode = (Integer) runtimeResult.get("exitCode");
            usedTime = (Integer) runtimeResult.get("usedTime");
            usedMemory = (Integer) runtimeResult.get("usedMemory");
        } catch (Exception ex) {
            LOGGER.warn("exception thrown while getRuntimeResult: " + ex.toString());
        }
        result.put("exitCode", exitCode);
        result.put("usedTime", usedTime);
        result.put("usedMemory", usedMemory);
        return result;
    }

    /**
     * 获取待执行的命令行.
     *
     * @param submission    - 评测记录对象
     * @param workDirectory - 编译生成结果的目录以及程序输出的目录
     * @param baseFileName  - 待执行的应用程序文件名(不包含文件后缀)
     * @return 待执行的命令行
     */
    private String getCommandLine(ExtendSubmission submission, String workDirectory, String baseFileName) {
        ExtendLanguage language = submission.getLanguage();
        String filePathWithoutExtension = String.format("%s/%s", workDirectory, baseFileName);
        StringBuilder runCommand = new StringBuilder(
                language.getRunCommand().replace("{filename}", filePathWithoutExtension));
        if (language.getLanguageName().equalsIgnoreCase("Java")) {
            int lastIndexOfSpace = runCommand.lastIndexOf("/");
            runCommand.setCharAt(lastIndexOfSpace, ' ');
        }
        return runCommand.toString();
    }

    /**
     * 根据不同语言获取最大时间限制.
     *
     * @param submission - 评测记录对象
     * @return 最大时间限制
     */
    private int getTimeLimit(ExtendSubmission submission) {
        ExtendLanguage language = submission.getLanguage();
        int timeLimit = 1500;
        if (language.getLanguageName().equalsIgnoreCase("Java"))
            timeLimit *= 2;
        return timeLimit;
    }

    /**
     * 根据不同语言获取最大空间限制.
     *
     * @param submission - 评测记录对象
     * @return 最大空间限制
     */
    private int getMemoryLimit(ExtendSubmission submission) {
        return 65535;
    }

    /**
     * 获取(编译)程序运行结果.
     *
     * @param commandLine    - 待执行程序的命令行
     * @param outputFilePath - 输出文件路径(可为NULL)
     * @param timeLimit      - 时间限制(单位ms, 0表示不限制)
     * @param memoryLimit    - 内存限制(单位KB, 0表示不限制)
     * @return 一个包含程序运行结果的 {@code Map<String, Object>} 对象
     */
    Map<String, Object> getRuntimeResult(String commandLine, String outputFilePath, int timeLimit, int memoryLimit) {
        Map<String, Object> result = null;
        try {
            result = getRuntimeResult(commandLine, systemUsername, systemPassword,
                    null, outputFilePath, timeLimit, memoryLimit);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 获取程序运行结果.
     *
     * @param commandLine    - 待执行程序的命令行
     * @param systemUsername - 登录操作系统的用户名
     * @param systemPassword - 登录操作系统的密码
     * @param inputFilePath  - 输入文件路径(可为NULL)
     * @param outputFilePath - 输出文件路径(可为NULL)
     * @param timeLimit      - 时间限制(单位ms, 0表示不限制)
     * @param memoryLimit    - 内存限制(单位KB, 0表示不限制)
     * @return 一个包含程序运行结果的 {@code Map<String, Object>} 对象
     */
    public native Map<String, Object> getRuntimeResult(String commandLine,
                                                       String systemUsername,
                                                       String systemPassword,
                                                       String inputFilePath,
                                                       String outputFilePath,
                                                       int timeLimit,
                                                       int memoryLimit);

    /**
     * 登录操作系统的用户名.
     * 为了安全, 我们建议评测程序以低权限的用户运行.
     */
    private static final String systemUsername = (String)
            ConfigSystem.getInstance().getCommandConfig("Execute", "Execute_User_Username");

    /**
     * 登录操作系统的密码.
     * 为了安全, 我们建议评测程序以低权限的用户运行.
     */
    private static final String systemPassword = (String)
            ConfigSystem.getInstance().getCommandConfig("Execute", "Execute_User_Password");

    /**
     * 日志记录器.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    static {
        try {
            NativeLibraryLoader.loadLibrary("JudgerCore");
        } catch (Exception ex) {
            LOGGER.error("load native library failed : " + ex.toString());
        }
    }
}
