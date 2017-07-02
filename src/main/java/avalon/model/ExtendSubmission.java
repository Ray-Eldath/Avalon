package avalon.model;

/**
 * Created by Eldath Ray on 2017/6/24 0024.
 *
 * @author Eldath Ray
 */
public class ExtendSubmission {
    private long submitTime;
    private String code;
    private ExtendLanguage language;

    public ExtendSubmission(String code, ExtendLanguage language) {
        this.submitTime = System.currentTimeMillis();
        this.code = code;
        this.language = language;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public String getCode() {
        return code;
    }

    public ExtendLanguage getLanguage() {
        return language;
    }
}
