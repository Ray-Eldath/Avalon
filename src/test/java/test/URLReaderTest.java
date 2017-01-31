package test;

import jdk.nashorn.api.scripting.URLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eldath on 2017/1/31 0031.
 *
 * @author Eldath
 */
public class URLReaderTest {
    public static void main(String[] args) {
        String thisLine;
        String[] split;
        HashMap<String, String> hashMap = new HashMap<>();
        String urlString = "http://git.oschina.net/ProgramLeague/Image/raw/master/Avalon/ShowMsg.properties";
        try (BufferedReader br = new BufferedReader(new URLReader(new URL(urlString), StandardCharsets.UTF_8))) {
            while ((thisLine = br.readLine()) != null) {
                if (!thisLine.contains("=") || thisLine.charAt(0) == '#') continue;
                split = thisLine.split("=");
                hashMap.put(split[0], split[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + " : " + value);
        }
    }
}
