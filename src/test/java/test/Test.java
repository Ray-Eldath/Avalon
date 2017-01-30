package test;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(LocalDateTime.parse(LocalDate.now().getYear() + "-11-11T12:12:12").toString());
    }
}
