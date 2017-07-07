package avalon.model.hook;

/**
 * Created by Eldath Ray on 2017/4/17 0017.
 * <p>
 * ALL: All messages received will run this hook.
 * ADMIN_ONLY: Only messages from admin (Admin accounts set in Avalon Config File) will run the hook.
 * GENERAL_USER_ONLY: Only message NOT from admin will run the hook.
 *
 * @author Eldath Ray
 */
public enum HookType {
    ALL, ADMIN_ONLY, GENERAL_USER_ONLY
}
