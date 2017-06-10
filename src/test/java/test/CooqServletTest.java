package test;

import avalon.util.servlet.CoolqServlet;

/**
 * Created by Eldath Ray on 2017/6/10 0010.
 *
 * @author Eldath Ray
 */
public class CooqServletTest {
    public static void main(String[] args) throws Exception {
        CoolqServlet servlet = new CoolqServlet();
       /*
        BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(servlet.apiAddress() + "/get_group_member_info?group_id=" + 399863405 + "&user_id=" +
                951394653).openStream()));
        System.out.println(reader.readLine());
        reader = new BufferedReader(new InputStreamReader(new URL(
                servlet.apiAddress() +
                        "/send_group_msg?group_id=" + 617118724 +
                        "&message=" +
                        UrlEncoded.encodeString("This is a test message sent by Avalon - Cooq.")).openStream()));
        System.out.println(reader.readLine());*/
        // System.out.println(servlet.getGroupSenderCardName(399863405, 951394653));
        servlet.responseGroup(617118724, "This is a test message sent by Avalon - Cooq.");
    }
}
