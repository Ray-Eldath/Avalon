package avalon.tool.pool;

/**
 * Created by Eldath on 2017/1/28 0028.
 *
 * @author Eldath
 */
public class VariablePool {
    private VariablePool() {
    }

    public static int Mo_Count = 0;
    public static boolean Mo_Reach_Max = false;
    public static boolean Limit_Noticed = false;

    public static class GameMode {
        public static boolean Is_Game_Mode_On = false;
        public static boolean Is_Loaded = false;
    }
}
