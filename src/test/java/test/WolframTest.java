package test;

import avalon.tool.WolframGetter;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/6/11 0011.
 *
 * @author Eldath Ray
 */
public class WolframTest {
    public static void main(String[] args) throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        List<WolframGetter.WolframPod> pods = WolframGetter.get(builder.build(
                new File("F:/query.xml")).getRootElement());
        for (WolframGetter.WolframPod thisPod : pods) {
            System.out.println(thisPod.getTitle());
            System.out.println(thisPod.getPlaintext());
            System.out.print("---");
        }
    }
}