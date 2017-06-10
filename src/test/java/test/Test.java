package test;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class Test {
    public static void main(String[] args) {
        String listen = "http://127.0.0.1:5000/";
        System.out.println(listen.replace("http://", "").split("/")[1]);
    }
}
