package avalon.model.executive;

import java.io.Serializable;

/**
 * 编程语言的Model.
 *
 * @author Haozhe Xie
 */
public class ExecutiveLanguage implements Serializable {

    /**
     * 编程语言的构造函数.
     *
     * @param languageName   - 编程语言的名称
     * @param compileCommand - 编程语言的编译命令
     * @param runCommand     - 编程语言对应程序执行命令
     */
    public ExecutiveLanguage(String languageName, String compileCommand, String runCommand) {
        this.languageName = languageName;
        this.compileCommand = compileCommand;
        this.runCommand = runCommand;
    }

    /**
     * 获取编程语言的名称.
     *
     * @return 编程语言的名称
     */
    public String getLanguageName() {
        return languageName;
    }

    /**
     * 获取编程语言的编译命令.
     */
    public String getCompileCommand() {
        return compileCommand;
    }

    /**
     * 获取编程语言的运行命令.
     *
     * @return 编程语言的运行命令
     */
    public String getRunCommand() {
        return runCommand;
    }

    /**
     * 编程语言的名称.
     */
    private String languageName;

    /**
     * 编程语言的编译命令.
     */
    private String compileCommand;

    /**
     * 编程语言的运行命令.
     */
    private String runCommand;

    /**
     * 唯一的序列化标识符
     */
    private static final long serialVersionUID = 9065824880175832696L;
}
