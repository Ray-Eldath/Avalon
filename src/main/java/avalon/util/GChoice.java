package avalon.util;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
public class GChoice extends GSection {
    private final int id;
    private final GOption[] options;

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

    @Override
    public String getString() {
        StringBuilder string = new StringBuilder();
        for (GOption thisOption : options)
            string.append(thisOption.getOption()).append(". ").append(thisOption.getQuestion()).append("\n");
        return "请选择：\n" + string.toString();
    }
}
