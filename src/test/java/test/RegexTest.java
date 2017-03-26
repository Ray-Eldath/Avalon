package test;

import java.util.HashMap;

/**
 * Created by Eldath on 2017/2/18 0018.
 *
 * @author Eldath
 */
public class RegexTest {
    static HashMap<String, String> stringStringHashMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(stringStringHashMap.size());
        new Test();
        System.out.println(stringStringHashMap.size());
        System.out.println(stringStringHashMap.get("a"));
    }

    public static HashMap<String, String> getStringStringHashMap() {
        return stringStringHashMap;
    }
}
