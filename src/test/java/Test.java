import org.eclipse.jetty.util.UrlEncoded;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        String content = "测试而已\n测试";
        System.out.print(UrlEncoded.encodeString(content));
    }
}
