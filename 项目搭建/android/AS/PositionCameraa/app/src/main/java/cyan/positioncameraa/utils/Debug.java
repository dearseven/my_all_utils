package cyan.positioncameraa.utils;

import android.util.Log;

/**
 * Created by wx on 2017/3/27.
 */

public class Debug {
    public static void log(Class clz, String log) {
        Log.i("--log--", clz.getName() + "\n" + log);
    }

    public static void exception(Class clz, String log) {
        Log.i("CatchExcep", clz.getName() + "\n" + log);
    }
}