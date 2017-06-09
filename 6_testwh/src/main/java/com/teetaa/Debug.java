package com.teetaa;

import android.util.Log;

/**
 * Created by wx on 2017/4/18.
 */

public class Debug {
    public static void i(Class clz,String log){
        Log.i("fenghuangtv",clz.getName());
        Log.i("fenghuangtv",log);
    }

    public static void d(Class clz,String log){
        Log.d("fenghuangtv",clz.getName());
        Log.d("fenghuangtv",log);
    }
}
