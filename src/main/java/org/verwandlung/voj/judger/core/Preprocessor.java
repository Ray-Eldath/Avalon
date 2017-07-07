package org.verwandlung.voj.judger.core;

import avalon.model.executive.ExecutiveLanguage;
import avalon.model.executive.ExecutiveSubmission;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eldath Ray on 2017/6/24 0024.
 *
 * @author Eldath Ray
 */
public class Preprocessor {
    /**
     * 创建测试代码至本地磁盘.
     *
     * @param submission    - 评测记录对象
     * @param workDirectory - 用于产生编译输出的目录
     * @param baseFileName  - 随机文件名(不包含后缀)
     */
    public void createTestCode(ExecutiveSubmission submission, String workDirectory, String baseFileName) throws Exception {
        File workDirFile = new File(workDirectory);

        if (!workDirFile.exists() && !workDirFile.mkdirs())
            throw new IOException("failed to create directory: " + workDirectory);

        ExecutiveLanguage language = submission.getLanguage();
        String code = replaceClassName(language, submission.getCode(), baseFileName);
        String codeFilePath = String.format("%s/%s.%s", workDirectory, baseFileName, getCodeFileSuffix(language));

        BufferedWriter writer = new BufferedWriter(new FileWriter(codeFilePath));
        for (String thisLine : code.split("\n")) {
            writer.write(thisLine);
            writer.newLine();
        }
        IOUtils.closeQuietly(writer);
    }

    /**
     * 获取代码文件的后缀名.
     *
     * @param language - 编程语言对象
     * @return 代码文件的后缀名
     */
    private String getCodeFileSuffix(ExecutiveLanguage language) {
        String compileCommand = language.getCompileCommand();
        Pattern pattern = Pattern.compile("\\{filename}\\.((?!exe| ).)+");
        Matcher matcher = pattern.matcher(compileCommand);
        if (matcher.find()) {
            String sourceFileName = matcher.group();
            return sourceFileName.replaceAll("\\{filename}\\.", "");
        }
        return "";
    }

    /**
     * 替换部分语言中的类名(如Java), 以保证正常通过编译.
     *
     * @param language     - 编程语言对象
     * @param code         - 待替换的代码
     * @param newClassName - 新的类名
     */
    private String replaceClassName(ExecutiveLanguage language, String code, String newClassName) {
        if (!language.getLanguageName().toLowerCase().equals("java"))
            return code;
        return code.replaceAll("class[ \n]+Main", "class " + newClassName);
    }

    /**
     * 设置代码文件所在目录的读写权限.
     * 在Linux下, 代码以UID=1536的用户运行, 因此需要为Others用户组分配写权限.
     *
     * @param workDirectory 用于产生编译输出的目录
     */
    private void setWorkDirectoryPermission(File workDirectory) throws IOException {
        Set<PosixFilePermission> permissions = new HashSet<>();

        permissions.add(PosixFilePermission.OWNER_READ);
        permissions.add(PosixFilePermission.OWNER_WRITE);
        permissions.add(PosixFilePermission.OWNER_EXECUTE);

        permissions.add(PosixFilePermission.GROUP_READ);
        permissions.add(PosixFilePermission.GROUP_WRITE);
        permissions.add(PosixFilePermission.GROUP_EXECUTE);

        permissions.add(PosixFilePermission.OTHERS_READ);
        permissions.add(PosixFilePermission.OTHERS_WRITE);
        permissions.add(PosixFilePermission.OTHERS_EXECUTE);

        Files.setPosixFilePermissions(workDirectory.toPath(), permissions);
    }
}
