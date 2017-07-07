package avalon.model.executive;

/**
 * Created by Eldath Ray on 2017/6/24 0024.
 *
 * @author Eldath Ray
 */
public class ExecutiveSubmission {
    private long submitTime;
    private String code;
    private ExecutiveLanguage language;

    public ExecutiveSubmission(String code, ExecutiveLanguage language) {
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

    public ExecutiveLanguage getLanguage() {
        return language;
    }
}
