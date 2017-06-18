package avalon.tool;

import org.eclipse.jetty.util.UrlEncoded;
import org.jdom2.Element;
import org.jdom2.xpath.XPathDiagnostic;
import org.jdom2.xpath.XPathFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eldath Ray on 2017/6/11 0011.
 *
 * @author Eldath Ray
 */
public class WolframGetter {
    public static class WolframPod {
        private String title, id, plaintext;

        WolframPod(String title, String id, String plaintext) {
            this.title = UrlEncoded.decodeString(title);
            this.id = id;
            this.plaintext = plaintext;
        }

        public String getTitle() {
            return title;
        }

        public String getId() {
            return id;
        }

        public String getPlaintext() {
            return plaintext;
        }
    }

    public static List<WolframPod> get(Element root) {
        XPathFactory xPath = XPathFactory.instance();
        XPathDiagnostic<Object> objects = xPath.compile("//subpod").diagnose(root, false);
        List<WolframPod> result = new ArrayList<>();
        objects.getResult().forEach(e -> {
            Element e1 = (Element) e;
            result.add(
                    new WolframPod(handleString(e1.getAttributeValue("title")),
                            handleString(e1.getAttributeValue("id")),
                            handleString(e1.getChild("plaintext").getValue())
                    ));
        });
        return result;
    }

    private static String handleString(String i) {
        if (i == null)
            return "";
        return i.replaceAll(" +", " ");
    }
}
