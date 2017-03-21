package util;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
public class GChoice {
    private int id;
    private GOption[] options;

    public GChoice(int id, GOption[] options) {
        this.id = id;
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public GOption[] getOptions() {
        return options;
    }
}
