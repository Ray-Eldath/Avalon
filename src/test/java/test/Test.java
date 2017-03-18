package test;

import util.Option;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) {
        new Option('A', () -> {
            System.out.println("Hello!");
            return false;
        });
    }
}
