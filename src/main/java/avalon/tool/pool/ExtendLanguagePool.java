package avalon.tool.pool;

import avalon.model.ExtendLanguage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath Ray on 2017/6/24 0024.
 *
 * @author Eldath Ray
 */
public class ExtendLanguagePool {
    private Map<String, ExtendLanguage> map = new HashMap<>(10, 3);
    private static final ExtendLanguagePool instance = new ExtendLanguagePool();

    public static ExtendLanguagePool getInstance() {
        return instance;
    }

    private ExtendLanguagePool() {
        map.put("cpp11", new ExtendLanguage(
                "cpp11",
                "g++ -O2 -Wall -std=c++11 -o {filename}.exe {filename}.cpp -lm",
                "{filename}.exe"));
        map.put("c99", new ExtendLanguage(
                "cpp",
                "g++ -O2 -Wall -std=c99 -o {filename}.exe {filename}.cpp -lm",
                "{filename}.exe"));
        map.put("py", new ExtendLanguage(
                "py",
                "python -m py_compile {filename}.py",
                "python {filename}.py"));
        map.put("java", new ExtendLanguage(
                "java",
                "javac {filename}.java",
                "java -cp {filename}"));
    }

    public ExtendLanguage get(String key) {
        return map.get(key.toLowerCase());
    }
}
