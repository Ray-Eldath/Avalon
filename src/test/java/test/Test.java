package test;

import java.time.LocalTime;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) {
        LocalTime thisTime = LocalTime.parse("20:15:13");
        System.out.println(thisTime.plusHours(24).toString());
    }
}
