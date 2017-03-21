package util;

import java.util.function.Consumer;

/**
 * Created by Eldath Ray on 2017/3/21.
 *
 * @author Eldath Ray
 */
public class GInteraction {
    private int id;
    private String question, answer;
    private Consumer<Boolean> rightSequel, wrongSequel;

    public GInteraction(int id, String question, String answer, Consumer<Boolean> rightSequel, Consumer<Boolean> wrongSequel) {
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
}
