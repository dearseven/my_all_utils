package cc.m2u.alternator_monitor1.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/5/24.
 */
public class DebugLog {
    private final static boolean DEV = true;

    public static void i(Class clz, String log) {
        if (DEV) {
            Log.i("alternator_monitor1", clz.getName());
            Log.i("alternator_monitor1", log);
        }
    }

    public static void d(Class clz, String log) {
        if (DEV) {
            Log.d("alternator_monitor1", clz.getName());
            Log.d("alternator_monitor1", log);
        }
    }


}
