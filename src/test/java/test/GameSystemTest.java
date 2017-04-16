package test;

import util.GOption;

/**
 * Created by Eldath Ray on 2017/3/20.
 *
 * @author Eldath Ray
 */
class GameSystemTest {
    public static void main(String[] args) {
        new GOption('A', "", (a) -> System.out.println("Hello World!"));
    }
}
