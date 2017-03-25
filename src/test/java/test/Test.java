package test;

import tool.ConstantPool;

import java.net.URL;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) throws Exception {
        URL url = new URL(ConstantPool.Address.weChatAPIServer + "/openwx/stop_client");
        System.out.println(url.toString());
        url.openStream();
    }
}
