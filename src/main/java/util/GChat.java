package util;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
public class GChat {
    private GPerson talker;
    private String content;

    public GChat(GPerson talker, String content) {
        this.talker = talker;
        this.content = content;
    }

    public GPerson getTalker() {
        return talker;
    }

    public String getContent() {
        return content;
    }
}
