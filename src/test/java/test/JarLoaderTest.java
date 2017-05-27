package test;

import avalon.util.AvalonPlugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import static avalon.tool.pool.ConstantPool.Address.dataPath;

/**
 * Created by Eldath Ray on 2017/5/27 0027.
 *
 * @author Eldath Ray
 */
public class JarLoaderTest {
    public static void main(String[] args) throws Exception {
        URL url = new URL("file:" + dataPath + File.separator +
                "plugin" + File.separator + "Avalon-Game_main.jar");
        URLClassLoader myClassLoader1 = new URLClassLoader(new URL[]{url}, Thread.currentThread()
                .getContextClassLoader());
        Class<?> aClass = myClassLoader1.loadClass("avalon.game.main.AvalonPlugin");
        AvalonPlugin plugin = (AvalonPlugin) aClass.newInstance();
        plugin.main();
    }
}
