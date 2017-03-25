package test;

import tool.GameScriptLoader;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println(GameScriptLoader.getInstance().getScript(0).getString());
    }
}
