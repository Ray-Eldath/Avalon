package util;

import java.util.function.Consumer;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GTextBox extends GSection {
    private final int id;
    private final String question;
    private final String answer;
    private final Consumer<Boolean> rightSequel;
    private final Consumer<Boolean> wrongSequel;

    public GTextBox(int id, String question, String answer, Consumer<Boolean> rightSequel, Consumer<Boolean> wrongSequel) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.rightSequel = rightSequel;
        this.wrongSequel = wrongSequel;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Consumer<Boolean> getRightSequel() {
        return rightSequel;
    }

    public Consumer<Boolean> getWrongSequel() {
        return wrongSequel;
    }

    @Override
    public String getString() {
        return "请键入您的回答：\n" + question;
    }
}
