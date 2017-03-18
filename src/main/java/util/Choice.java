package util;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
public class Choice {
    private int id;
    private Option[] options;

    public Choice(int id, Option[] options) {
        this.id = id;
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public Option[] getOptions() {
        return options;
    }
}
