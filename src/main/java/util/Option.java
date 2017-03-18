package util;

import java.util.function.BooleanSupplier;

/**
 * Created by Eldath Ray on 2017/3/19 0019.
 *
 * @author Eldath Ray
 */
public class Option {
    private char option;
    private BooleanSupplier sequel;

    public Option(char option, BooleanSupplier sequel) {
        this.option = option;
        this.sequel = sequel;
    }

    public char getOption() {
        return option;
    }

    public boolean doSequel() {
        return sequel.getAsBoolean();
    }
}
