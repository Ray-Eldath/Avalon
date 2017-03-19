package test;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("http://127.0.0.1:8080".replace("http://", "").split(":")[1]);
    }
}
