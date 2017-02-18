package test;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/2/18 0018.
 *
 * @author Eldath
 */
public class RegexTest {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("啦啦啦|lizhaohan");
        System.out.println(pattern.matcher("lizhaohan").find());
        System.out.println(pattern.toString());
    }
}
