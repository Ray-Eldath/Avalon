package test;

import org.eclipse.jetty.util.UrlEncoded;

import java.net.URL;

import static avalon.tool.pool.Constants.Basic.currentServlet;

/**
 * Created by Eldath Ray on 2017/6/10 0010.
 *
 * @author Eldath Ray
 */
public class CoolqServletTest {
    public static void main(String[] args) throws Exception {
//         CoolQServlet servlet = new CoolQServlet();
        String cq = UrlEncoded.encodeString("[CQ:image,file=file://D:\\Users\\Eldath\\Pictures\\表情包\\tt.png]");
        new URL(currentServlet.apiAddress() + "/send_group_msg?group_id=617118724&message=TT" + cq).openStream();
        // System.out.println(servlet.getGroupSenderNickname(399863405, 951394653));
        // servlet.responseGroup(617118724, "This is a test message sent by Avalon - Cooq.");
    }
}
