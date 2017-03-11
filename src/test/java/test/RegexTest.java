package test;

import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/2/18 0018.
 *
 * @author Eldath
 */
public class RegexTest {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("\\w?(\\+1s|-1s|膜蛤|苟|续命|州长夫人)\\w?");
        System.out.println(pattern.matcher("啦啦啦+1s").find());
    }
}
