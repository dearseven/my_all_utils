package cc.m2u.ifengbigdata.utils;

import android.util.Log;

/**
 * Created by wx on 2017/3/27.
 */

public class DLog {

    public static void log(Class clz, String log) {
        Log.i("ifengbd", clz.getName() + "\n" + log);
    }

    public static void exception(Class clz, String log) {
        Log.i("CatchExcep", clz.getName() + "\n" + log);
    }
}