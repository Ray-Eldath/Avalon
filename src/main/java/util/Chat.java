package util;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
public class Chat {
    private Person talker;
    private String content;

    public Chat(Person talker, String content) {
        this.talker = talker;
        this.content = content;
    }

    public Person getTalker() {
        return talker;
    }

    public String getContent() {
        return content;
    }
}
